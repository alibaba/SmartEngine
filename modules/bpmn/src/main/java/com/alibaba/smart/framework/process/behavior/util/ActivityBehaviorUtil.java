package com.alibaba.smart.framework.process.behavior.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.smart.framework.engine.assembly.Transition;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;
import com.alibaba.smart.framework.process.behavior.ActivityBehavior;
import com.alibaba.smart.framework.process.model.bpmn.assembly.activity.SequenceFlow;
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ConditionExpression;
import com.alibaba.smart.framework.process.session.ExecutionSession;
import com.alibaba.smart.framework.process.session.util.ThreadLocalExecutionSessionUtil;

public class ActivityBehaviorUtil {

    /**
     * 是否需要针对普通activi 和 网关的 activity 做出不同的方法? TODO
     * 添加debug日志,添加event和interceptor
     */
    public void leaveCurrentActivity() {

        ExecutionSession executionSession = ThreadLocalExecutionSessionUtil.get();
        RuntimeActivity currentRuntimeActivity = executionSession.getCurrentRuntimeActivity();
        Map<String, RuntimeTransition> outcomeTransitions = currentRuntimeActivity.getOutcomeTransitions();

        List<RuntimeTransition> toBeChoosenRuntimeTransition = new ArrayList<>();

        Set<Entry<String, RuntimeTransition>> transitionEntries = outcomeTransitions.entrySet();

        for (Entry<String, RuntimeTransition> entry : transitionEntries) {
            RuntimeTransition runtimeTransition = entry.getValue();
            // 和Model的关系 ,定义域参与运行域?
            Transition transition = runtimeTransition.getModel();

            // TODO 顶层模型支持sequenceFlow,避免强转
            SequenceFlow sequenceFlow = (SequenceFlow) transition;
            ConditionExpression conditionExpression = sequenceFlow.getConditionExpression();
            if (null == conditionExpression) {
                // TODO 支持通过隐式的的transitionId来判断
                toBeChoosenRuntimeTransition.add(runtimeTransition);
            } else {
                String expressionType = conditionExpression.getExpressionType();
                String expressionContent = conditionExpression.getExpressionContent();

                ConditionExpressionEvaluater conditionExpressionEvaluater = ConditionExpressionEvaluaterFactory.createConditionExpression(expressionType);
                boolean conditionPassed = conditionExpressionEvaluater.evaluate(expressionContent);
                if (conditionPassed) {
                    toBeChoosenRuntimeTransition.add(runtimeTransition);
                }
            }

        }
        
        if(toBeChoosenRuntimeTransition.size() ==1){
            RuntimeTransition outgoingRuntimeTransition =   toBeChoosenRuntimeTransition.get(0);
            RuntimeActivity targetRuntimeActivity=  outgoingRuntimeTransition.getTarget();
            
            //TODO 有点tricky
            ThreadLocalExecutionSessionUtil.get().setCurrentRuntimeActivity(targetRuntimeActivity);
           
            String activityClassName = targetRuntimeActivity.getModel().getClass().getName();
            ActivityBehavior activityBehavior =  ActivityBehaviorRegister.getActivityBehavior(activityClassName);
            activityBehavior.execute();
            
            
        } else if (toBeChoosenRuntimeTransition.size() >1){
            //FIXME 需要支持并行网关
            RuntimeTransition outgoingRuntimeTransition =   toBeChoosenRuntimeTransition.get(0);
            RuntimeActivity targetRuntimeActivity=  outgoingRuntimeTransition.getTarget();
            
            ThreadLocalExecutionSessionUtil.get().setCurrentRuntimeActivity(targetRuntimeActivity);
            
            String activityClassName = targetRuntimeActivity.getModel().getClass().getName();
            ActivityBehavior activityBehavior =  ActivityBehaviorRegister.getActivityBehavior(activityClassName);
            activityBehavior.execute();
            
        }else{
            
            //TODO 把日志和当前请求参数打印好. 
            throw new RuntimeException("No outgoing transitions found for "+currentRuntimeActivity+executionSession);
            
        }
        
        

    }
}

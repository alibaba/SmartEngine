package com.alibaba.smart.framework.process.behavior.util;

import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.process.behavior.ActivityBehavior;
import com.alibaba.smart.framework.process.session.ExecutionSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author 高海军 帝奇
 */
public class ActivityBehaviorUtil {

    public void leaveCurrentActivity() {

        ExecutionSession executionSession = null;// ThreadLocalUtil.get();
        PvmActivity currentRuntimeActivity = executionSession.getCurrentRuntimeActivity();
        Map<String, PvmTransition> outcomeTransitions = currentRuntimeActivity.getOutcomeTransitions();

        List<PvmTransition> toBeChoosenRuntimeTransition = new ArrayList<>();

        Set<Entry<String, PvmTransition>> transitionEntries = outcomeTransitions.entrySet();

        for (Entry<String, PvmTransition> entry : transitionEntries) {
            PvmTransition runtimeTransition = entry.getValue();

            Transition transition = runtimeTransition.getModel();

            SequenceFlow sequenceFlow = (SequenceFlow) transition;
            ConditionExpression conditionExpression = sequenceFlow.getConditionExpression();
            if (null == conditionExpression) {
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

        if (toBeChoosenRuntimeTransition.size() == 1) {
            PvmTransition outgoingRuntimeTransition = toBeChoosenRuntimeTransition.get(0);
            PvmActivity targetRuntimeActivity = outgoingRuntimeTransition.getTarget();

//            ThreadLocalUtil.get().setCurrentRuntimeActivity(targetRuntimeActivity);

            String activityClassName = targetRuntimeActivity.getModel().getClass().getName();
            ActivityBehavior activityBehavior = ActivityBehaviorRegister.getActivityBehavior(activityClassName);
            activityBehavior.execute();

        } else if (toBeChoosenRuntimeTransition.size() > 1) {
            // FIXME 需要支持并行网关
            PvmTransition outgoingRuntimeTransition = toBeChoosenRuntimeTransition.get(0);
            PvmActivity targetRuntimeActivity = outgoingRuntimeTransition.getTarget();

//            ThreadLocalUtil.get().setCurrentRuntimeActivity(targetRuntimeActivity);

            String activityClassName = targetRuntimeActivity.getModel().getClass().getName();
            ActivityBehavior activityBehavior = ActivityBehaviorRegister.getActivityBehavior(activityClassName);
            activityBehavior.execute();

        } else {

            // TODO 把日志和当前请求参数打印好.
            throw new RuntimeException("No outgoing transitions found for " + currentRuntimeActivity + executionSession);

        }

    }
}

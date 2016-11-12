package com.alibaba.smart.framework.process.behavior.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.process.behavior.ActivityBehavior;
import com.alibaba.smart.framework.process.session.ExecutionSession;

/**
 * @author 高海军 帝奇
 */
public class ActivityBehaviorUtil {

    public void leaveCurrentActivity() {

        ExecutionSession executionSession = null;// ThreadLocalUtil.get();
        PvmActivity currentRuntimeActivity = executionSession.getCurrentRuntimeActivity();
        Map<String, PvmTransition> outcomeTransitions = currentRuntimeActivity.getOutcomeTransitions();

        List<PvmTransition> toBeChosenRuntimeTransition = new ArrayList<>();

        Set<Entry<String, PvmTransition>> transitionEntries = outcomeTransitions.entrySet();

        for (Entry<String, PvmTransition> entry : transitionEntries) {
            PvmTransition runtimeTransition = entry.getValue();

            Transition transition = runtimeTransition.getModel();

            SequenceFlow sequenceFlow = (SequenceFlow) transition;
            ConditionExpression conditionExpression = sequenceFlow.getConditionExpression();
            if (null == conditionExpression) {
                toBeChosenRuntimeTransition.add(runtimeTransition);
            } else {
                String expressionType = conditionExpression.getExpressionType();
                String expressionContent = conditionExpression.getExpressionContent();

                ConditionExpressionEvaluater conditionExpressionEvaluater = ConditionExpressionEvaluaterFactory.createConditionExpression(expressionType);
                boolean conditionPassed = conditionExpressionEvaluater.evaluate(expressionContent);
                if (conditionPassed) {
                    toBeChosenRuntimeTransition.add(runtimeTransition);
                }
            }
        }

        if (toBeChosenRuntimeTransition.size() == 1) {
            PvmTransition outgoingRuntimeTransition = toBeChosenRuntimeTransition.get(0);
            PvmActivity targetRuntimeActivity = outgoingRuntimeTransition.getTarget();

//            ThreadLocalUtil.get().setCurrentRuntimeActivity(targetRuntimeActivity);

            String activityClassName = targetRuntimeActivity.getModel().getClass().getName();
            ActivityBehavior activityBehavior = ActivityBehaviorRegister.getActivityBehavior(activityClassName);
            activityBehavior.execute(null);

        } else if (toBeChosenRuntimeTransition.size() > 1) {
            // FIXME 需要支持并行网关
            PvmTransition outgoingRuntimeTransition = toBeChosenRuntimeTransition.get(0);
            PvmActivity targetRuntimeActivity = outgoingRuntimeTransition.getTarget();

//            ThreadLocalUtil.get().setCurrentRuntimeActivity(targetRuntimeActivity);

            String activityClassName = targetRuntimeActivity.getModel().getClass().getName();
            ActivityBehavior activityBehavior = ActivityBehaviorRegister.getActivityBehavior(activityClassName);
            activityBehavior.execute(null);

        } else {

            // TODO 把日志和当前请求参数打印好.
            throw new RuntimeException("No outgoing transitions found for " + currentRuntimeActivity + executionSession);

        }

    }
}

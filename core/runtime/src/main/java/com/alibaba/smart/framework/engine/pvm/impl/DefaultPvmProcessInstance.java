package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
public class DefaultPvmProcessInstance implements PvmProcessInstance {

    @Override
    public ProcessInstance start(ExecutionContext executionContext) {

        PvmProcessDefinition pvmProcessDefinition = executionContext.getPvmProcessDefinition();
        PvmActivity pvmStartActivity = pvmProcessDefinition.getStartActivity();
        return this.startProcessInstance(pvmStartActivity, executionContext);

    }

//    @Override
//    public void run(ExecutionContext context) {
//        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();
//        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
//        ProcessInstance processInstance = context.getProcessInstance();
//        String processInstanceId = processInstance.getInstanceId();
//        ActivityInstance activityInstance = activityInstanceFactory.create();
//        activityInstance.setProcessInstanceId(processInstanceId);
//        processInstance.setStatus(InstanceStatus.running);
//        activityInstance.setCurrentStep(String.valueOf(PvmEventConstant.ACTIVITY_START.getCode()));
//        activityInstance.setActivityId(context.getCurrentExecution().getActivity().getActivityId());
//        context.getCurrentExecution().setActivity(activityInstance);
//        PvmProcessDefinition pvmProcessDefinition=     context.getPvmProcessDefinition();
//        Map<String,PvmActivity> activityMap = pvmProcessDefinition.getActivities();
//        if (activityMap.isEmpty() || !activityMap.containsKey(context.getCurrentExecution().getActivity().getActivityId())) {
//            throw new EngineException("not have this activity!");
//        }
//        this.startProcessInstance(activityMap.get(context.getCurrentExecution().getActivity().getActivityId()),context);
//
//
//    }

    private ProcessInstance startProcessInstance(PvmActivity startActivity, ExecutionContext context) {
        this.executeCurrentActivityAndLookupNextTransitionRecursively(startActivity, context);

        return context.getProcessInstance();
    }

    private void executeCurrentActivityAndLookupNextTransitionRecursively(PvmActivity pvmActivity, ExecutionContext context) {

        //重要: 执行当前节点,会触发当前节点的行为执行
        pvmActivity.execute(context);

        if (context.isNeedPause()) {
            // break;
            return;
        }

        // 执行后续节点选择,实际上会调用此类 DefaultActivityTransitionSelectInvoker
//        pvmActivity.fireEvent(PvmEventConstant.ACTIVITY_TRANSITION_SELECT.name(),
//                                                                 context);

        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();

        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            List<PvmTransition> matchedTransitions = new ArrayList<>(outcomeTransitions.size());
            for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                PvmTransition pvmTransition1 = transitionEntry.getValue();
                TransitionProvider invokerProvider = (TransitionProvider) pvmTransition1.getProvider();
                boolean matched = invokerProvider.execute(pvmTransition1, context);

                if (matched) {
                    matchedTransitions.add(pvmTransition1);
                }

                for (PvmTransition matchedTransition : matchedTransitions) {
                    PvmActivity  targetPvmActivity =   matchedTransition.getTarget();
                    this.executeCurrentActivityAndLookupNextTransitionRecursively(targetPvmActivity, context);
                }
            }

            //FIXME
//            if (null != "") {
//                Object transitionSelectBody = null;
//                if (null != transitionSelectBody && transitionSelectBody instanceof List) {
//                    List<?> executionObjects = (List<?>) transitionSelectBody;
//                    if (!executionObjects.isEmpty()) {
//
//                        for (Object executionObject : executionObjects) {
//                            // 执行所有实例
//                            if (executionObject instanceof ExecutionInstance) {
//                                ExecutionInstance executionInstance = (ExecutionInstance) executionObject;
//                                context.setCurrentExecution(executionInstance);
//
//                                TransitionInstance transitionInstance = executionInstance.getActivity().getIncomeTransitions().get(0);
//                                PvmTransition pvmTransition = pvmActivity.getOutcomeTransitions().get(transitionInstance.getTransitionId());
//                                // 执行Transition,目前仅是fireEvent,没做其他逻辑
//                                pvmTransition.execute(context);
//                                PvmActivity targetPvmActivity = pvmTransition.getTarget();
//
//                                //重要:递归调用 执行Activity
//                                this.executeCurrentActivityAndLookupNextTransitionRecursively(targetPvmActivity, context);
//                            } else {
//                                throw new EngineException("unsupported class type:ExecutionInstance");
//                            }
//                        }
//
//                    }
//                }
//            }
        }


    }
}

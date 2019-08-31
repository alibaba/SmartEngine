package com.alibaba.smart.framework.engine.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.expression.evaluator.MvelExpressionEvaluator;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.AbstractGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇
 * @date 2019.08.16
 * 根据当前环节id等参数，获取该节点的直接后向环节。 如果直接后向环节是网关型节点，则会继续查找网关型节点的后向节点；否则会返回参数中的该直接后向环节。。
 */
public class ProcessSimulation {

    private SmartEngine smartEngine;
    //TODO 应该是单例
    private MvelExpressionEvaluator mvelExpressionEvaluator = new MvelExpressionEvaluator();

    public ProcessSimulation(SmartEngine smartEngine) {
        this.smartEngine = smartEngine;
    }

    /**
     * 根据当前环节id等参数，获取该节点的直接后向环节。 如果直接后向环节是网关型节点，则会继续查找网关型节点的后向节点；否则会返回参数中的该直接后向环节。
     *
     * @param processDefinitionId
     * @param processDefinitionVersion
     * @param currentActivityId
     * @param simulationContext        执行互斥网关的条件表达式参数
     * @return
     */
    public List<Activity> simulateOutcomingActivities(String processDefinitionId, String processDefinitionVersion,
                                                      String currentActivityId, Map<String, Object> simulationContext) {

        PvmProcessDefinition pvmProcessDefinition = smartEngine.getProcessEngineConfiguration()
            .getExtensionPointRegistry().getExtensionPoint(
                ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinitionId,
                processDefinitionVersion);

        PvmActivity currentPvmActivity = pvmProcessDefinition.getActivities().get(currentActivityId);

        if (null == currentPvmActivity) {
            String exceptionMessage = String.format(
                "No PvmActivity found for processDefinitionId :%BehaviorUtil,processDefinitionVersion :%BehaviorUtil,"
                    + "currentActivityId :%BehaviorUtil",
                processDefinitionId, processDefinitionVersion, currentActivityId);
            throw new EngineException(exceptionMessage);

        }
        List<Activity> resultList = new ArrayList<Activity>();

        simulation(currentPvmActivity, simulationContext, resultList);

        return resultList;
    }

    void simulation(PvmActivity currentPvmActivity, Map<String, Object> simulationContext, List<Activity> resultList) {

        Map<String, PvmTransition> outComingPvmTransitionMap = currentPvmActivity.getOutcomeTransitions();
        if (null == outComingPvmTransitionMap) {
            // 这种情况下，currentPvmActivity 应该是EndEvent了
            assert currentPvmActivity.getModel().getClass().equals(EndEvent.class);
            //do nothing，
        } else {
            for (Entry<String, PvmTransition> pvmTransitionEntry : outComingPvmTransitionMap.entrySet()) {
                PvmTransition pvmTransition = pvmTransitionEntry.getValue();
                PvmActivity newCurrentPvmActivity = pvmTransition.getTarget();
                Activity newCurrentActivity = newCurrentPvmActivity.getModel();

                //如果新的当前节点是网关型节点
                if (newCurrentActivity instanceof AbstractGateway) {

                    if (newCurrentActivity instanceof ExclusiveGateway) {
                        Map<String, PvmTransition> outcomeTransitions = newCurrentPvmActivity.getOutcomeTransitions();

                        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                            PvmTransition pendingTransition = transitionEntry.getValue();
                            SequenceFlow sequenceFlow = (SequenceFlow)pendingTransition.getModel();
                            ConditionExpression conditionExpression = sequenceFlow.getConditionExpression();

                            boolean matched = (Boolean)mvelExpressionEvaluator.eval(
                                conditionExpression.getExpressionContent(), simulationContext);

                            if (matched) {
                                Activity chosenActivity = pendingTransition.getTarget().getModel();

                                if (chosenActivity instanceof AbstractGateway) {
                                    //                        //如果后向直接节点是互斥网关，或者并行网关。
                                    //                        那么需要递归处理，因为理论上来讲，互斥网关的节点仍然可能存在互斥网关。那么则要需要再继续分析网关型的后向节点。
                                    simulation(newCurrentPvmActivity, simulationContext, resultList);
                                } else {
                                    resultList.add(chosenActivity);

                                }

                            }

                        }
                    } else if (newCurrentActivity instanceof ParallelGateway) {
                        Map<String, PvmTransition> outcomeTransitions = newCurrentPvmActivity.getOutcomeTransitions();

                        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                            PvmTransition pendingTransition = transitionEntry.getValue();
                            Activity activity = pendingTransition.getTarget().getModel();
                            resultList.add(activity);

                        }
                    } else {
                        throw new EngineException("Not support:" + newCurrentActivity);
                    }

                } else {
                    resultList.add(newCurrentActivity);
                }
            }
        }

    }

}

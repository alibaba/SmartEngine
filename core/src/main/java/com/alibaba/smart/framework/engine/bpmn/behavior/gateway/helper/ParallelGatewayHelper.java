package com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper;

import com.alibaba.smart.framework.engine.behavior.ActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.ParallelGatewayBehavior;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import java.util.*;


public abstract class ParallelGatewayHelper {

    private static final String DEFAULT = "default";

    // 判断是否为网关
    public static boolean isParallelGateway(ActivityBehavior activityBehavior) {
       boolean pg =  activityBehavior instanceof ParallelGatewayBehavior;
       return  pg;
    }



    // 验证网关
    public static void validateGateways(ProcessDefinition processDefinition,PvmProcessDefinition pvmProcessDefinition) {
        List<BaseElement> elements = processDefinition.getBaseElementList();

        if (null != elements && !elements.isEmpty()) {
        Stack<ParallelGateway> gatewayStack = new Stack<>();

        for (BaseElement flowNode : elements) {
            if (flowNode instanceof ParallelGateway) {
                ParallelGateway gateway = (ParallelGateway) flowNode;
                PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(gateway.getId());

                // 如果是 Split 网关，压栈
                if (isForkGateway(pvmActivity,pvmProcessDefinition)) {
                    gatewayStack.push(gateway);
                }
                // 如果是 Join 网关，检查栈顶是否匹配
                else if (isJoinGateway(pvmActivity,pvmProcessDefinition)) {
                    if (gatewayStack.isEmpty()) {
                        throw new EngineException("Unpaired Join Gateway: " + gateway.getId());
                    }
                    ParallelGateway lastSplit = gatewayStack.pop();
                    if (!isMatchingGateway(lastSplit, gateway)) {
                        throw new EngineException("Mismatched Gateways: " + lastSplit.getId() + " and " + gateway.getId());
                    }
                }
            }
        }

        // 检查是否有未闭合的 Split 网关
        if (!gatewayStack.isEmpty()) {
            throw new EngineException("Unclosed fork Gateway: " + gatewayStack.peek());
        }
        }
    }

    // 判断是否为 Split 网关
    public static boolean isForkGateway(PvmActivity pvmActivity, PvmProcessDefinition pvmProcessDefinition) {

        int inComeTransitionSize = pvmActivity.getIncomeTransitions().size();
        int outComeTransitionSize = pvmActivity.getOutcomeTransitions().size();
        return inComeTransitionSize == 1 && outComeTransitionSize > 1;
    }

    // 判断是否为 Join 网关
    public static boolean isJoinGateway( PvmActivity pvmActivity,PvmProcessDefinition pvmProcessDefinition) {


        int inComeTransitionSize = pvmActivity.getIncomeTransitions().size();
        int outComeTransitionSize = pvmActivity.getOutcomeTransitions().size();
        return inComeTransitionSize > 1 &&  outComeTransitionSize == 1;
    }

    // 判断网关是否匹配
    public static boolean isMatchingGateway(ParallelGateway split, ParallelGateway join) {
        throw new EngineException("bug");
    }

    public static Map<String,String> findMatchedJoinGateway(PvmProcessDefinition pvmProcessDefinition) {
        Map<String,String> resultMap = new HashMap<String,String>();


        Map<String, PvmActivity> pvmActivityMap = pvmProcessDefinition.getActivities();

        //获得所有的ParallelGateway
        List<ParallelGateway> elementListByType = getElementListByType(pvmActivityMap, ParallelGateway.class);


        for (ParallelGateway parallelGateway : elementListByType) {
            PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(parallelGateway.getId());

            //仅针对fork网关进行处理
            if( isForkGateway(pvmActivity,pvmProcessDefinition)){

                //如果是子fork节点,那么该节点应该在递归中处理完毕. 这里不用重复处理
                if(null == resultMap.get(pvmActivity.getModel().getId())){

                    findOutAllForkJoinPairs( pvmActivity,pvmProcessDefinition,resultMap);
                }

            }
        }



        return  resultMap;
    }

    private static void findOutAllForkJoinPairs(PvmActivity forkPvmActivity, PvmProcessDefinition pvmProcessDefinition, Map<String,String> resultMap ) {


        Map<String, PvmTransition> outcomeTransitions = forkPvmActivity.getOutcomeTransitions();

        //针对所有分支处理
        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {

            PvmTransition pendingTransition = transitionEntry.getValue();
            PvmActivity mayBeJoinTarget = pendingTransition.getTarget();


            mayBeJoinTarget =  filterNonParallelGateway(mayBeJoinTarget,pvmProcessDefinition);

            //  遇到的fork网关 ,说明该分支有嵌套fork,则递归进入
            if(isForkGateway(mayBeJoinTarget,pvmProcessDefinition)){
                //递归处理
                findOutAllForkJoinPairs(mayBeJoinTarget,pvmProcessDefinition,resultMap);
            } else  if(isJoinGateway(mayBeJoinTarget, pvmProcessDefinition)){
                resultMap.put(forkPvmActivity.getModel().getId(),mayBeJoinTarget.getModel().getId());
                break;
            }else{
                // do nothing
            }

        }
    }

    private static PvmActivity filterNonParallelGateway(PvmActivity currentPvmActivity, PvmProcessDefinition pvmProcessDefinition){

        //如果当前节点就是ParallelGateway时,则立即返回
        if(isForkGateway(currentPvmActivity,pvmProcessDefinition) || isJoinGateway(currentPvmActivity,pvmProcessDefinition)){
            return currentPvmActivity;
        }


        Map<String, PvmTransition> outcomeTransitions = currentPvmActivity.getOutcomeTransitions();

        //针对所有分支处理
        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {

            PvmTransition pendingTransition = transitionEntry.getValue();
            PvmActivity mayBeJoinTarget = pendingTransition.getTarget();

            if (isForkGateway(mayBeJoinTarget,pvmProcessDefinition) || isJoinGateway(mayBeJoinTarget,pvmProcessDefinition)){
                //return 只会跳出当前递归调用，而不会跳出整个循环或递归栈。 所以还需要 在else分支中处理下
                return mayBeJoinTarget;
            }else{
                // 如果既不是fork,也不是join,则继续遍历target的后向节点
                // 递归处理,在下一个判断中继续寻找分支上的后续节点(这里可能存在互斥网关,所以需要在递归内遍历所有分支)
                PvmActivity result = filterNonParallelGateway(mayBeJoinTarget, pvmProcessDefinition);
                if (result != null) {
                    // 如果递归调用找到了目标网关，立即返回
                    return result;
                }
            }


        }

        throw new EngineException("should find one parallel gateway :"+ currentPvmActivity.getModel().getId());
    }




    public static <T extends Activity> List<T> getElementListByType(Map<String, PvmActivity> pvmActivityMap,Class<T> elementType) {
        List<T> list = new ArrayList();

        for (Map.Entry<String, PvmActivity> pvmActivityEntry : pvmActivityMap.entrySet()) {
            PvmActivity pvmActivity = pvmActivityEntry.getValue();

            Activity activity = pvmActivity.getModel();

            if(elementType.isInstance(activity)){
                list.add(elementType.cast(activity));
            }
        }

        return list;
    }


//    @Override
//    public void execute(ActivityExecution execution) {
//        // 获取当前网关
//        ParallelGateway gateway = (ParallelGateway) execution.getActivity();
//
//        // 如果是 Join 网关
//        if (isJoinGateway(gateway)) {
//            // 找到对应的 Split 网关
//            ParallelGateway splitGateway = findMatchingSplitGateway(gateway, execution);
//
//            // 等待所有分支到达
//            if (allBranchesCompleted(splitGateway, execution)) {
//                leave(execution); // 继续执行后续流程
//            }
//        }
//    }


//    // 找到对应的 Split 网关
//    private ParallelGateway findMatchingSplitGateway(ParallelGateway joinGateway, ActivityExecution execution) {
//        // 获取流程定义模型
//        BpmnModelInstance modelInstance = execution.getProcessDefinition().getBpmnModelInstance();
//
//        // 遍历流程定义，找到与 Join 网关匹配的 Split 网关
//        for (FlowNode flowNode : modelInstance.getModelElementsByType(FlowNode.class)) {
//            if (flowNode instanceof ParallelGateway && isMatchingSplit(flowNode, joinGateway)) {
//                return (ParallelGateway) flowNode;
//            }
//        }
//        throw new ProcessEngineException("No matching Split Gateway found for Join Gateway: " + joinGateway.getId());
//    }

//    // 判断是否为匹配的 Split 网关
//    private boolean isMatchingSplit(BaseElement flowNode, ParallelGateway joinGateway) {
//        if (flowNode instanceof ParallelGateway) {
//            ParallelGateway splitGateway = (ParallelGateway) flowNode;
//            // 检查 Split 网关的输出流是否包含 Join 网关的输入流
//            for (SequenceFlow outgoingFlow : splitGateway.getOutgoingFlows()) {
//                if (outgoingFlow.getTarget().equals(joinGateway)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    // 检查所有分支是否完成
//    private boolean allBranchesCompleted(ParallelGateway splitGateway, ActivityExecution execution) {
//        // 获取 Split 网关的所有输出流
//        List<SequenceFlow> outgoingFlows = splitGateway.getOutgoingFlows();
//
//        // 检查每条分支是否已完成
//        for (SequenceFlow flow : outgoingFlows) {
//            if (!isFlowCompleted(flow, execution)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    // 检查某条分支是否已完成
//    private boolean isFlowCompleted(SequenceFlow flow, ActivityExecution execution) {
//        // 通过执行实例的上下文检查分支状态
//        return execution.findExecutions(flow.getTarget().getId()).isEmpty();
//    }

}

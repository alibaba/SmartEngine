package com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper;

import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import java.util.*;


public abstract class ParallelGatewayHelper {


    // 判断是否为 Fork 网关
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


    public static Map<String,String> findMatchedJoinGateway(PvmProcessDefinition pvmProcessDefinition) {
        Map<String,String> resultMap = new HashMap();


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



}

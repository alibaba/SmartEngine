package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.common.id.generator.IdGenerator;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ParallelGatewayBehavior extends AbstractActivityBehavior<ParallelGateway> implements ActivityBehavior<ParallelGateway> {

    public ParallelGatewayBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }


    @Override
    public void buildInstanceRelationShip(PvmActivity pvmActivity, ExecutionContext context) {
        //算法说明:ParallelGatewayBehavior 同时承担 fork 和 join 职责。所以说,如何判断是 fork 还是 join ?
        // 目前主要原则就看pvmActivity节点的 incomeTransition 和 outcomeTransition 的比较。
        // 如果 income 为1,则为 join 节点。
        // 如果 outcome 为 1 ,则为 fork 节点。
        // 重要:在流程定义解析时,需要判断如果是 fork,则 outcome >=2, income=1; 类似的,如果是 join,则 outcome = 1,income>=2

        ProcessInstance processInstance = context.getProcessInstance();


        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();
        Map<String, PvmTransition> incomeTransitions = pvmActivity.getIncomeTransitions();

        int outComeTransitionSize = outcomeTransitions.size();
        int inComeTransitionSize = incomeTransitions.size();
        if (outComeTransitionSize >= 2 && inComeTransitionSize == 1) {
            //fork

            IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();
            context.setBlockId(idGenerator.getId());

            ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, context);

            context.getProcessInstance().addNewActivityInstance(activityInstance);


        } else if (outComeTransitionSize == 1 && inComeTransitionSize >= 2) {

            //old2(pvmActivity, context, processInstance, incomeTransitions);

            PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = super.getExtensionPointRegistry().getExtensionPoint(PersisterFactoryExtensionPoint.class);

            ExecutionInstanceStorage executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);


            Collection<PvmTransition> inComingPvmTransitions = incomeTransitions.values();


            String sourcePvmActivityId = context.getSourcePvmActivity().getModel().getId();

            for (PvmTransition pvmTransition : inComingPvmTransitions) {
                String pvmTransitionSourceActivityId = pvmTransition.getSource().getModel().getId();
                boolean equals1 = pvmTransitionSourceActivityId.equals(sourcePvmActivityId);
                if (equals1) {
                    ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, context);

                    ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance,  context);

                    activityInstance.setExecutionInstance(executionInstance);

                    context.getProcessInstance().addNewActivityInstance(activityInstance);
                }
            }

            List<ExecutionInstance> executionInstanceList = executionInstanceStorage.findActiveExecution(processInstance.getInstanceId());

            int reachedForkedSum = 0;

            if(null != executionInstanceList){

                for (ExecutionInstance executionInstance : executionInstanceList) {

                  if(executionInstance.getActivityId().equals(pvmActivity.getModel().getId())){
                            reachedForkedSum = reachedForkedSum+1;
                   }
                }
            }

           //如果已经完成
           if(reachedForkedSum == inComingPvmTransitions.size()){
             //把当前停留在join节点的执行实例全部complete掉,然后再持久化时,会自动忽略掉这些节点。
               ActivityInstanceStorage activityInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);

               List<ActivityInstance> activityInstanceList = activityInstanceStorage.findAll(processInstance.getInstanceId());

               for (ActivityInstance activityInstance : activityInstanceList) {

                       if(activityInstance.getActivityId().equals(pvmActivity.getModel().getId())){
                           MarkDoneUtil.markDone(activityInstance,activityInstance.getExecutionInstance(),super.getExtensionPointRegistry());
                       }
               }


           }else{
           //未完成的话,流程继续暂停
               context.setNeedPause(true);
           }



        } else {
            throw new IllegalStateException("code should touch here for ParallelGateway:" + pvmActivity);
        }


    }

    //private void old2(PvmActivity pvmActivity, ExecutionContext context, ProcessInstance processInstance,
    //                  Map<String, PvmTransition> incomeTransitions) {//join
    //
    //    Long blockId = context.getBlockId();
    //
    //    if (null == blockId) {
    //        throw new EngineException("blockId should not be blank when in fork activity");
    //    }
    //
    //    //注意: 这里并没有判断并发情况,需要业务开发者自己在业务线程中加锁控制。否则会导致两个节点都完成了,但是流程节点并没有驱动下去。
    //    // 判断当前db中的,blockId是否相同(相同的blockId表示是一对 fork,join 网关) 以及 当前pvmActivity 的activityInstance数量 是否与 (inComeTransitionSize -1) 差相等。
    //    // 如果相等,则说明该fork网关职责结束,可以前往下一个节点。否则,跳出当前执行逻辑。
    //
    //    PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = super.getExtensionPointRegistry().getExtensionPoint(PersisterFactoryExtensionPoint.class);
    //
    //    ActivityInstanceStorage activityInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);
    //
    //    List<ActivityInstance> activityInstanceList = activityInstanceStorage.findAll(processInstance.getInstanceId());
    //
    //    Collection<PvmTransition> pvmTransitions = incomeTransitions.values();
    //
    //    boolean isComplete = true;
    //
    //    String sourcePvmActivityId = context.getSourcePvmActivity().getModel().getId();
    //
    //    if (null != activityInstanceList) {
    //        //遍历当前所有的活动实例
    //        for (ActivityInstance aliveActivityInstance : activityInstanceList) {
    //            //遍历当前join节点的incoming activityId
    //            for (PvmTransition pvmTransition : pvmTransitions) {
    //                String pvmTransitionSourceActivityId = pvmTransition.getSource().getModel().getId();
    //                String aliveActivityId = aliveActivityInstance.getActivityId();
    //                boolean equals1 = pvmTransitionSourceActivityId.equals(sourcePvmActivityId);
    //                //boolean equals2 = aliveActivityId.equals(pvmTransitionSourceActivityId);
    //                //boolean equals3 = null == aliveActivityInstance.getCompleteDate();
    //                if (equals1) {
    //                    ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, context);
    //
    //                    ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance,  context);
    //
    //                    activityInstance.setExecutionInstance(executionInstance);
    //
    //                    context.getProcessInstance().addNewActivityInstance(activityInstance);
    //                }
    //            }
    //        }
    //    }
    //
    //    if(!isComplete){
    //        context.setNeedPause(true);
    //    }
    //}
    //
    //private void old1(ExecutionContext context, Map<String, PvmTransition> incomeTransitions,
    //                  List<ActivityInstance> activityInstanceList) {
    //    if (null != activityInstanceList) {
    //        Collection<PvmTransition> pvmTransitions = incomeTransitions.values();
    //
    //        ListIterator<ActivityInstance> listIterator = activityInstanceList.listIterator();
    //    String sourcePvmActivityId = context.getSourcePvmActivity().getModel().getId();
    //
    //        while (listIterator.hasNext()){
    //            ActivityInstance completeActivityInstance =listIterator.next();
    //            for (PvmTransition pvmTransition : pvmTransitions) {
    //                String activityId = pvmTransition.getSource().getModel().getId();
    //
    //
    //                //如果相等,说明当前节点是join 网关的前置节点之一
    //                if (activityId.equals(sourcePvmActivityId)) {
    //                    //如果相等,说明在同一个fork,join 环内。
    //                    //if (blockId.equals(completeActivityInstance.getBlockId())) {
    //
    //                        //如果不为空,那么则说明当前环节已经完成。 TODO 增加activity的active状态
    //                        //if (null != completeActivityInstance.getCompleteDate()) {
    //                            listIterator.remove();
    //                            break;
    //
    //                            //sum++;
    //                        }
    //                    }
    //                }
    //
    //            }
    //    //}
    //
    //    //for (ActivityInstance completeActivityInstance : activityInstanceList) {
    //    //    for (PvmTransition pvmTransition : pvmTransitions) {
    //    //        String activityId = pvmTransition.getSource().getModel().getId();
    //    //
    //    //        //如果相等,说明当前节点是join 网关的前置节点之一
    //    //        if (activityId.equals(completeActivityInstance.getActivityId())) {
    //    //            //如果相等,说明在同一个fork,join 环内。
    //    //            if (blockId.equals(completeActivityInstance.getBlockId())) {
    //    //
    //    //                //如果不为空,那么则说明当前环节已经完成。 TODO 增加activity的active状态
    //    //                if (null != completeActivityInstance.getCompleteDate()) {
    //    //                    sum++;
    //    //                }
    //    //            }
    //    //        }
    //    //
    //    //    }
    //    //}
    //    //}
    //
    //    //ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, context);
    //    //
    //    //context.getProcessInstance().addNewActivityInstance(activityInstance);
    //
    //    //所有节点已经完成
    //    if (activityInstanceList.isEmpty()) {
    //        //do nothing
    //    } else {
    //        context.setNeedPause(true);
    //    }
    //}

}

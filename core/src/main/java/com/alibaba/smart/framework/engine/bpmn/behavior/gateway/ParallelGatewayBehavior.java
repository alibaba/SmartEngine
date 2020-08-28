package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import com.alibaba.smart.framework.engine.util.InheritableTaskWithCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ParallelGateway.class)

public class ParallelGatewayBehavior extends AbstractActivityBehavior<ParallelGateway> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelGatewayBehavior.class);


    public ParallelGatewayBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        //算法说明:ParallelGatewayBehavior 同时承担 fork 和 join 职责。所以说,如何判断是 fork 还是 join ?
        // 目前主要原则就看pvmActivity节点的 incomeTransition 和 outcomeTransition 的比较。
        // 如果 income 为1,则为 join 节点。
        // 如果 outcome 为 1 ,则为 fork 节点。
        // 重要:在流程定义解析时,需要判断如果是 fork,则 outcome >=2, income=1; 类似的,如果是 join,则 outcome = 1,income>=2

        ParallelGateway parallelGateway = (ParallelGateway)pvmActivity.getModel();

        Map<String, PvmTransition> incomeTransitions = pvmActivity.getIncomeTransitions();
        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();

        int outComeTransitionSize = outcomeTransitions.size();
        int inComeTransitionSize = incomeTransitions.size();
        if (outComeTransitionSize >= 2 && inComeTransitionSize == 1) {
            //fork
            ExecutorService executorService = context.getProcessEngineConfiguration().getExecutorService();
            if(null == executorService){
                //顺序执行fork
                for (Entry<String, PvmTransition> pvmTransitionEntry : outcomeTransitions.entrySet()) {
                    PvmActivity target = pvmTransitionEntry.getValue().getTarget();
                    target.enter(context);
                }
            }else{
                //并发执行fork

                List<PvmActivityTask> tasks = new ArrayList<PvmActivityTask>(outcomeTransitions.size());

                for (Entry<String, PvmTransition> pvmTransitionEntry : outcomeTransitions.entrySet()) {
                    PvmActivity target = pvmTransitionEntry.getValue().getTarget();

                    PvmActivityTask task = new PvmActivityTask(target,context);
                    tasks.add(task);
                }


                try {
                    executorService.invokeAll(tasks);
                } catch (InterruptedException e) {
                    throw new EngineException(e.getMessage(), e);
                }

            }

        } else if (outComeTransitionSize == 1 && inComeTransitionSize >= 2) {
            //join 时必须使用分布式锁。

            LockStrategy lockStrategy = context.getProcessEngineConfiguration().getLockStrategy();
            String processInstanceId = context.getProcessInstance().getInstanceId();
            try{
                lockStrategy.tryLock(processInstanceId);

                super.enter(context, pvmActivity);



                Collection<PvmTransition> inComingPvmTransitions = incomeTransitions.values();


                ProcessInstance processInstance = context.getProcessInstance();

                //当前内存中的，新产生的 active ExecutionInstance
                List<ExecutionInstance> executionInstanceListFromMemory = InstanceUtil.findActiveExecution(processInstance);


                //当前持久化介质中中，已产生的 active ExecutionInstance。
                List<ExecutionInstance> executionInstanceListFromDB =  executionInstanceStorage.findActiveExecution(processInstance.getInstanceId(), super.processEngineConfiguration);

                //Merge 数据库中和内存中的EI。如果是 custom模式，则可能会存在重复记录，所以这里需要去重。 如果是 DataBase 模式，则不会有重复的EI.

                List<ExecutionInstance> mergedExecutionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceListFromMemory.size());


                for (ExecutionInstance instance : executionInstanceListFromDB) {
                    if (executionInstanceListFromMemory.contains(instance)){
                        //ignore
                    }else {
                        mergedExecutionInstanceList.add(instance);
                    }
                }


                mergedExecutionInstanceList.addAll(executionInstanceListFromMemory);


                int reachedJoinCounter = 0;
                List<ExecutionInstance> chosenExecutionInstances = new ArrayList<ExecutionInstance>(executionInstanceListFromMemory.size());

                if(null != mergedExecutionInstanceList){

                    for (ExecutionInstance executionInstance : mergedExecutionInstanceList) {

                        if (executionInstance.getProcessDefinitionActivityId().equals(parallelGateway.getId())) {
                            reachedJoinCounter++;
                            chosenExecutionInstances.add(executionInstance);
                        }
                    }
                }


                if(reachedJoinCounter == inComingPvmTransitions.size() ){
                    //把当前停留在join节点的执行实例全部complete掉,然后再持久化时,会自动忽略掉这些节点。

                    if(null != chosenExecutionInstances){
                        for (ExecutionInstance executionInstance : chosenExecutionInstances) {
                            MarkDoneUtil.markDoneExecutionInstance(executionInstance,executionInstanceStorage,
                                processEngineConfiguration);
                        }
                    }

                    return false;

                }else{
                    //未完成的话,流程继续暂停
                    return true;
                }

            }finally {

                lockStrategy.unLock(processInstanceId);
            }

        }else{
            throw new EngineException("should touch here:"+pvmActivity);
        }

        return true;

    }

    class PvmActivityTask extends InheritableTaskWithCache {
        private PvmActivity pvmActivity;
        private ExecutionContext context;

        PvmActivityTask(PvmActivity pvmActivity,ExecutionContext context) {
            this.pvmActivity = pvmActivity;
            this.context = context;
        }

        @Override
        public void runTask() {
            try {
                pvmActivity.enter(context);
            } catch (Exception e) {
                LOGGER.error( e.getMessage(),e);
            }
        }
    }



/*
    @Override
    public void buildInstanceRelationShip(ExecutionContext context) {


        ParallelGateway parallelGateway = this.getModel();
        PvmActivity pvmActivity = this.getPvmActivity();

        ProcessInstance processInstance = context.getProcessInstance();


        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();
        Map<String, PvmTransition> incomeTransitions = pvmActivity.getIncomeTransitions();

        int outComeTransitionSize = outcomeTransitions.size();
        int inComeTransitionSize = incomeTransitions.size();
        if (outComeTransitionSize >= 2 && inComeTransitionSize == 1) {
            //fork
            ActivityInstance activityInstance = super.activityInstanceFactory.create(parallelGateway, context);
            context.getProcessInstance().addNewActivityInstance(activityInstance);

        } else if (outComeTransitionSize == 1 && inComeTransitionSize >= 2) {

            //old2(pvmActivity, context, processInstance, incomeTransitions);

            PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = super.getExtensionPointRegistry().getExtensionPoint(PersisterFactoryExtensionPoint.class);

            ExecutionInstanceStorage executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);


            Collection<PvmTransition> inComingPvmTransitions = incomeTransitions.values();


            String sourcePvmActivityId = context.getSourcePvmActivity().getModel().getId();

            for (PvmTransition pvmActivity : inComingPvmTransitions) {
                String pvmTransitionSourceActivityId = pvmActivity.getSource().getModel().getId();
                boolean equals1 = pvmTransitionSourceActivityId.equals(sourcePvmActivityId);
                if (equals1) {
                    ActivityInstance activityInstance = super.activityInstanceFactory.create(parallelGateway, context);

                    ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance,  context);

                    activityInstance.setExecutionInstance(executionInstance);

                    context.getProcessInstance().addNewActivityInstance(activityInstance);
                }
            }

            List<ExecutionInstance> executionInstanceList = executionInstanceStorage.findActiveExecutionList(processInstance.getInstanceId());

            int reachedForkedSum = 0;

            if(null != executionInstanceList){

                for (ExecutionInstance executionInstance : executionInstanceList) {

                    if (executionInstance.getActivityId().equals(parallelGateway.getId())) {
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
    */

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
    //            for (PvmTransition pvmActivity : pvmTransitions) {
    //                String pvmTransitionSourceActivityId = pvmActivity.getSource().getModel().getId();
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
    //            for (PvmTransition pvmActivity : pvmTransitions) {
    //                String activityId = pvmActivity.getSource().getModel().getId();
    //
    //
    //                //如果相等,说明当前节点是join 网关的前置节点之一
    //                if (activityId.equals(sourcePvmActivityId)) {
    //                    //如果相等,说明在同一个fork,join 环内。
    //                    //if (blockId.equals(completeActivityInstance.getBlockId())) {
    //
    //                        //如果不为空,那么则说明当前环节已经完成。
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
    //    //    for (PvmTransition pvmActivity : pvmTransitions) {
    //    //        String activityId = pvmActivity.getSource().getModel().getId();
    //    //
    //    //        //如果相等,说明当前节点是join 网关的前置节点之一
    //    //        if (activityId.equals(completeActivityInstance.getActivityId())) {
    //    //            //如果相等,说明在同一个fork,join 环内。
    //    //            if (blockId.equals(completeActivityInstance.getBlockId())) {
    //    //
    //    //                //如果不为空,那么则说明当前环节已经完成。
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

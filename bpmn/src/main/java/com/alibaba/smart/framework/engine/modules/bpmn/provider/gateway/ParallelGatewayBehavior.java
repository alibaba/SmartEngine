package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import java.util.Collection;
import java.util.List;
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

            Long blockId = InstanceIdUtil.simpleId();
            context.setBlockId(blockId);
            ActivityInstance activityInstance = super.activityInstanceFactory.createWithBlockId(pvmActivity, context.getProcessInstance(), blockId);

            context.getProcessInstance().addNewActivityInstance(activityInstance);


        } else if (outComeTransitionSize == 1 && inComeTransitionSize >= 2) {
            //join

            Long blockId = context.getBlockId();

            if (null == blockId) {
                throw new EngineException("blockId should not be blank when in fork activity");
            }


            ActivityInstance activityInstance = super.activityInstanceFactory.createWithBlockId(pvmActivity, context);

            context.getProcessInstance().addNewActivityInstance(activityInstance);

            //TODO 这里如果会有并发的话,会导致两个节点都完成了,但是流程节点并没有驱动下去。
            // 判断当前db中的,blockId是否相同(相同的blockId表示是一对 fork,join 网关) 以及 当前pvmActivity 的activityInstance数量 是否与 (inComeTransitionSize -1) 差相等。
            // 如果相等,则说明该fork网关职责结束,可以前往下一个节点。否则,跳出当前执行逻辑。

            PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = super.getExtensionPointRegistry().getExtensionPoint(PersisterFactoryExtensionPoint.class);

            ActivityInstanceStorage activityInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);

            List<ActivityInstance> activityInstanceList = activityInstanceStorage.findAll(processInstance.getInstanceId());


            Integer sum = 0;

            if (null != activityInstanceList) {
                Collection<PvmTransition> pvmTransitions = incomeTransitions.values();

                for (ActivityInstance completeActivityInstance : activityInstanceList) {
                    for (PvmTransition pvmTransition : pvmTransitions) {
                        String activityId = pvmTransition.getSource().getModel().getId();

                        //如果相等,说明当前节点是join 网关的前置节点之一
                        if (activityId.equals(completeActivityInstance.getActivityId())) {
                            //如果相等,说明在同一个fork,join 环内。
                            if (blockId.equals(completeActivityInstance.getBlockId())) {

                                //如果不为空,那么则说明当前环节已经完成。 TODO 增加activity的active状态
                                if (null != completeActivityInstance.getCompleteDate()) {
                                    sum++;
                                }
                            }
                        }

                    }
                }
            }

            //所有节点已经完成
            if (sum == inComeTransitionSize) {
                //do nothing
            } else {
                context.setNeedPause(true);
            }


        } else {
            throw new IllegalStateException("code should touch here for ParallelGateway:" + pvmActivity);
        }


    }


}

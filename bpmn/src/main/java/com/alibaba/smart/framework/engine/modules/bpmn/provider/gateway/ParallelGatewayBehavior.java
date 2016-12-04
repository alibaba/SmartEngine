package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

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


        Map<String, PvmTransition>  outcomeTransitions = pvmActivity.getOutcomeTransitions();
        Map<String, PvmTransition>  incomeTransitions = pvmActivity.getIncomeTransitions();

        int outComeTransitionSize = outcomeTransitions.size();
        int inComeTransitionSize = incomeTransitions.size();
        if(outComeTransitionSize >=2 && inComeTransitionSize ==1){
            //fork
            for (Map.Entry<String, PvmTransition> pvmTransitionEntry : outcomeTransitions.entrySet()) {
                PvmTransition pvmTransition=   pvmTransitionEntry.getValue();
                PvmActivity targetPvmActivity=  pvmTransition.getTarget();

                ActivityInstance activityInstance = super.activityInstanceFactory.create(targetPvmActivity, processInstance);

                ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance);


                activityInstance.setExecutionInstance(executionInstance);

                processInstance.addActivityInstance(activityInstance);



            }

        }else if(outComeTransitionSize == 1 && inComeTransitionSize >=2){
            //join

        }else{
            throw new IllegalStateException("code should touch here for ParallelGateway:"+pvmActivity);
        }




        ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, context.getProcessInstance());

        context.getProcessInstance().addActivityInstance(activityInstance);
    }

    @Override
    public boolean needSuspend() {
        return false;
    }
}

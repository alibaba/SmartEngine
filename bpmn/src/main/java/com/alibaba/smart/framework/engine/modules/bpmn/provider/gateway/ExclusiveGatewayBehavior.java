package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ExclusiveGatewayBehavior extends AbstractActivityBehavior<ExclusiveGateway> implements ActivityBehavior<ExclusiveGateway> {

    public ExclusiveGatewayBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }


    @Override
    public void buildInstanceRelationShip(PvmActivity pvmActivity, ExecutionContext context) {

        //TUNE 流程节点不应该停留在该节点上。流转的职责可以更加聚焦下。
        ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, context);

        context.getProcessInstance().addNewActivityInstance(activityInstance);
    }


}

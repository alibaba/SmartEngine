package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * Created by niefeng on 2018/4/18.
 */
public class InclusiveGatewayBehavior  extends AbstractActivityBehavior<ExclusiveGateway> {
    public InclusiveGatewayBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

}

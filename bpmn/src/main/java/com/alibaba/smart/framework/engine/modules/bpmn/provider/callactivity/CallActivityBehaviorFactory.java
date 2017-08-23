package com.alibaba.smart.framework.engine.modules.bpmn.provider.callactivity;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class CallActivityBehaviorFactory implements ActivityProviderFactory<CallActivity> {

    private ExtensionPointRegistry extensionPointRegistry;

    public CallActivityBehaviorFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public CallActivityBehavior createActivityProvider(PvmActivity activity) {
        return new CallActivityBehavior(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<CallActivity> getModelType() {
        return CallActivity.class;
    }

}

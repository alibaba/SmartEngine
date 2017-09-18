package com.alibaba.smart.framework.engine.modules.smart.provider.performer;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.service.InstanceAccessService;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.performer.Java;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class JavaPerformerFactory implements PerformerProviderFactory<Java> {
    private ExtensionPointRegistry extensionPointRegistry;

    public JavaPerformerFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public Performer createPerformer(Java performable) {
        ProcessEngineConfiguration
            processEngineConfiguration = this.extensionPointRegistry.getExtensionPoint(SmartEngine.class)
            .getProcessEngineConfiguration();



        return new JavaPerformer(this.extensionPointRegistry, performable.getClassName());
    }

    @Override
    public Class<Java> getModelType() {
        return Java.class;
    }
}

package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.IntermediateCatchEvent;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 上午11:12
 */
public class IntermediateCatchEventProviderFactory implements ActivityProviderFactory<IntermediateCatchEvent> {

    private ExtensionPointRegistry extensionPointRegistry;

    public IntermediateCatchEventProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ActivityBehavior createActivityProvider(PvmActivity activity) {
        return new IntermediateCatchEventBehavior(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<IntermediateCatchEvent> getModelType() {
        return IntermediateCatchEvent.class;
    }
}

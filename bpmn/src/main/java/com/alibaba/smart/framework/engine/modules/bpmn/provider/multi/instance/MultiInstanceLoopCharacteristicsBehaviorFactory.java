package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.ExecutePolicy;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.provider.ExecutePolicyBehavior;
import com.alibaba.smart.framework.engine.provider.factory.ExecutePolicyProviderFactory;

/**
 * @author ettear
 * Created by ettear on 14/10/2017.
 */
public class MultiInstanceLoopCharacteristicsBehaviorFactory implements ExecutePolicyProviderFactory<MultiInstanceLoopCharacteristics> {

    private ExtensionPointRegistry extensionPointRegistry;

    public MultiInstanceLoopCharacteristicsBehaviorFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ExecutePolicyBehavior createExecutePolicyBehavior(MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {
        return new MultiInstanceLoopCharacteristicsBehavior(this.extensionPointRegistry,multiInstanceLoopCharacteristics);
    }

    @Override
    public Class<MultiInstanceLoopCharacteristics> getModelType() {
        return MultiInstanceLoopCharacteristics.class;
    }
}

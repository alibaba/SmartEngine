package com.alibaba.smart.framework.engine.modules.smart.provider.extension;

import java.util.List;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.ExecutionListener;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Value;
import com.alibaba.smart.framework.engine.provider.Invoker;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;

/**
 * Created by 高海军 帝奇 74394 on 2019 March  22:48.
 */
public abstract  class InvokerUtil {

    public static Invoker  createValueInvoker(ExtensionPointRegistry extensionPointRegistry,Value value) {
        return new ValueInvoker(extensionPointRegistry,value);
    }

    public static Invoker  createMultiValueInvoker(ExtensionPointRegistry extensionPointRegistry,
                                                   List<Value> extensionList) {
        return new MultiValueInvoker(extensionPointRegistry,extensionList);
    }

    public static Invoker createExecutionListenerInvoker(ExtensionPointRegistry extensionPointRegistry,
                                                         ProviderFactoryExtensionPoint providerFactoryExtensionPoint,ExecutionListener executionListener) {
        Performable performable = executionListener.getPerformable();
        PerformerProviderFactory providerFactory = (PerformerProviderFactory)providerFactoryExtensionPoint
            .getProviderFactory(performable.getClass());
        Performer performer = providerFactory.createPerformer(null, performable);
        return new ExecutionListenerInvoker(extensionPointRegistry, executionListener, performer);
    }

}
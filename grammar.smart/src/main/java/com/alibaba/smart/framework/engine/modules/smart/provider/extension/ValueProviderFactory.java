package com.alibaba.smart.framework.engine.modules.smart.provider.extension;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Value;
import com.alibaba.smart.framework.engine.provider.Invoker;
import com.alibaba.smart.framework.engine.provider.factory.InvokerProviderFactory;
/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class ValueProviderFactory implements
    InvokerProviderFactory<Value> {

    private ExtensionPointRegistry extensionPointRegistry;

    public ValueProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public Invoker createInvoker(Value value) {
        return new ValueInvoker(this.extensionPointRegistry,value);
    }

    @Override
    public Class<Value> getModelType() {
        return Value.class;
    }

}

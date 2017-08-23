package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.model.assembly.Invocable;
import com.alibaba.smart.framework.engine.provider.Invoker;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author ettear
 * Created by ettear on 02/08/2017.
 */
public interface InvokerProviderFactory<M extends Invocable> extends ProviderFactory<M>{
    /**
     * Create Invoker
     * @param invocable Invocable
     * @return Invoker
     */
    Invoker createInvoker(M invocable);
}

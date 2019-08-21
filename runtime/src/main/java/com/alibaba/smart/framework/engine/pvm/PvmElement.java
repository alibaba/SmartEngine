package com.alibaba.smart.framework.engine.pvm;

import java.util.List;

import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.model.assembly.Element;
import com.alibaba.smart.framework.engine.provider.Invoker;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmElement<M extends Element> extends Invoker, LifeCycleHook {

    M getModel();

    void setInvoker(Invoker invoker);

    void setPrepareExtensionInvokers(List<Invoker> prepareExtensionInvokers);

    void setExtensionInvokers(List<Invoker> extensionInvokers);
}

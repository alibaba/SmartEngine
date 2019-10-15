package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionBasedElement;
import com.alibaba.smart.framework.engine.provider.Invoker;
import com.alibaba.smart.framework.engine.pvm.PvmElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractPvmElement<M extends ExtensionBasedElement> implements PvmElement<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPvmElement.class);

    private M model;

    private Invoker invoker;
    private List<Invoker> prepareExtensionInvokers;
    private List<Invoker> extensionInvokers;

    protected ExtensionPointRegistry extensionPointRegistry;

    protected AbstractPvmElement(ExtensionPointRegistry extensionPointRegistry){
        this.extensionPointRegistry=extensionPointRegistry;
    }

    //@Override
    //public Object invoke(String event, ExecutionContext context) {
    //    // 执行准备周期扩展：如，参数转换，配置设置，预处理逻辑
    //    if (null != this.prepareExtensionInvokers) {
    //        for (Invoker prepareExtensionInvoker : prepareExtensionInvokers) {
    //            prepareExtensionInvoker.invoke(event, context);
    //        }
    //    }
    //
    //   Object result =  invokeBehavior(event, context);
    //
    //    // TODO 这个逻辑不合适。
    //    if (null != this.invoker) {
    //        result =  this.invoker.invoke(event, context);
    //
    //    }
    //    //TODO 增加事件发送机制 ettear
    //
    //    // 执行扩展：如，事件监听
    //    if (null != this.extensionInvokers) {
    //        for (Invoker extension : extensionInvokers) {
    //            extension.invoke(event, context);
    //        }
    //    }
    //
    //    return result;
    //}

    //protected abstract Object invokeBehavior(String event, ExecutionContext context);

    @Override
    public M getModel() {
        return this.model;
    }

    public void setModel(M model) {
        this.model = model;
    }

    public List<Invoker> getPrepareExtensionInvokers() {
        return prepareExtensionInvokers;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    @Override
    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public void setPrepareExtensionInvokers(
        List<Invoker> prepareExtensionInvokers) {
        this.prepareExtensionInvokers = prepareExtensionInvokers;
    }

    public List<Invoker> getExtensionInvokers() {
        return extensionInvokers;
    }

    @Override
    public void setExtensionInvokers(List<Invoker> extensionInvokers) {
        this.extensionInvokers = extensionInvokers;
    }
}

package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.Element;
import com.alibaba.smart.framework.engine.provider.Invoker;
import com.alibaba.smart.framework.engine.pvm.PvmElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractPvmElement<M extends Element> implements PvmElement<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPvmElement.class);

    private M model;

    private Invoker invoker;
    private List<Invoker> prepareExtensions;
    private List<Invoker> extensions;

    protected ExtensionPointRegistry extensionPointRegistry;

    protected AbstractPvmElement(ExtensionPointRegistry extensionPointRegistry){
        this.extensionPointRegistry=extensionPointRegistry;
    }

    @Override
    public Object invoke(String event, ExecutionContext context) {
        // 执行准备周期扩展：如，参数转换，配置设置，预处理逻辑
        if (null != this.prepareExtensions) {
            for (Invoker extension : prepareExtensions) {
                extension.invoke(event, context);
            }
        }

        // invoke
        Object result = invokeBehavior(event, context);
        if (null != this.invoker) {
            Object invokerResult = this.invoker.invoke(event, context);
            if (null == result) {
                result = invokerResult;
            }
        }
        //TODO 增加事件发送机制 ettear

        // 执行扩展：如，事件监听
        if (null != this.extensions) {
            for (Invoker extension : extensions) {
                extension.invoke(event, context);
            }
        }
        return result;
    }

    protected abstract Object invokeBehavior(String event, ExecutionContext context);

    @Override
    public M getModel() {
        return this.model;
    }

    public void setModel(M model) {
        this.model = model;
    }

    public List<Invoker> getPrepareExtensions() {
        return prepareExtensions;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    @Override
    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public void setPrepareExtensions(
        List<Invoker> prepareExtensions) {
        this.prepareExtensions = prepareExtensions;
    }

    public List<Invoker> getExtensions() {
        return extensions;
    }

    @Override
    public void setExtensions(List<Invoker> extensions) {
        this.extensions = extensions;
    }
}

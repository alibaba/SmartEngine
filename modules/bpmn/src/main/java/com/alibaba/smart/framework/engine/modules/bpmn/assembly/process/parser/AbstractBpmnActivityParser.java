package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.artifact.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.AbstractBpmnActivity;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnActivityParser<M extends AbstractBpmnActivity> extends AbstractBpmnParser<M> {

    public AbstractBpmnActivityParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected void parseChild(M model, BaseElement child) {
        if (child instanceof Handler) {
            model.setHandler((Handler) child);
        } else if (child instanceof ExtensionElements) {
            model.setExtensions((ExtensionElements) child);
        } else {
            this.parseModelChild(model, child);
        }
    }

    protected void parseModelChild(M model, BaseElement child) {

    }

}

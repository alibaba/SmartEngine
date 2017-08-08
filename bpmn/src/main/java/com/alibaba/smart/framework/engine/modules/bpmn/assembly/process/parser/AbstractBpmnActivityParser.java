package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.AbstractBpmnActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnActivityParser<M extends AbstractBpmnActivity> extends AbstractBpmnParser<M> {
    private final static String DEFAULT_ACTION = PvmEventConstant.ACTIVITY_EXECUTE.name();

    public AbstractBpmnActivityParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected String getDefaultActionName() {
        return DEFAULT_ACTION;
    }
}

package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExecutePolicy;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.AbstractTask;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnActivityParser<M extends AbstractActivity> extends AbstractBpmnParser<M> {
    private final static String DEFAULT_ACTION = PvmEventConstant.ACTIVITY_EXECUTE.name();

    public AbstractBpmnActivityParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected void parseChild(M model, BaseElement child) throws ParseException {
        if (child instanceof ExecutePolicy) {
            model.setExecutePolicy((ExecutePolicy)child);
            //TODO ettear remove
            if(child instanceof MultiInstanceLoopCharacteristics) {
                ((AbstractTask)model).setMultiInstanceLoopCharacteristics((MultiInstanceLoopCharacteristics)child);
            }
        }else {
            super.parseChild(model, child);
        }
    }

    @Override
    protected String getDefaultActionName() {
        return DEFAULT_ACTION;
    }
}

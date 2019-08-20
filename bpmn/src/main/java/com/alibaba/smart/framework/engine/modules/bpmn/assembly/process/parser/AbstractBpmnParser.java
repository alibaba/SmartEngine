package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Element;
import com.alibaba.smart.framework.engine.model.assembly.Extensions;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnParser<M extends Element> extends AbstractElementParser<M> {

    public AbstractBpmnParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected void parseChild(M model, BaseElement child) throws ParseException {
        if(!this.parseModelChild(model, child)) {
            if (child instanceof Extensions) {
                model.setExtensions((Extensions)child);
            } else if (child instanceof Performable) {
                List<Performable> performers = model.getPerformers();
                if (null == performers) {
                    performers = new ArrayList<Performable>();
                    model.setPerformers(performers);
                }
                Performable performable = (Performable)child;
                if (StringUtil.isEmpty(performable.getAction())) {
                    performable.setAction(getDefaultActionName());
                }
                performers.add(performable);
            }
        }
    }

    protected abstract String getDefaultActionName();

    /**
     *
     * @param model model
     * @param child child
     * @return circuit
     * @throws ParseException ParseException
     */
    protected boolean parseModelChild(M model, BaseElement child) throws ParseException {
        return false;
    }
}

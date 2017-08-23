package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Element;
import com.alibaba.smart.framework.engine.model.assembly.Extensions;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnParser<M extends Element> extends AbstractElementParser<M> {

    public AbstractBpmnParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected void parseChild(M model, BaseElement child) throws ParseException {
        if (child instanceof Extensions) {
            model.setExtensions((Extensions) child);
        } else if (child instanceof Performable) {
            List<Performable> performers = model.getPerformers();
            if (null == performers) {
                performers = new ArrayList<Performable>();
                model.setPerformers(performers);
            }
            Performable performable = (Performable)child;
            if (null == performable.getAction() || "".equals(performable.getAction())) {
                performable.setAction(getDefaultActionName());
            }
            performers.add(performable);
        } else {
            this.parseModelChild(model, child);
        }
    }

    protected abstract String getDefaultActionName();
    protected void parseModelChild(M model, BaseElement child) throws ParseException {

    }
}

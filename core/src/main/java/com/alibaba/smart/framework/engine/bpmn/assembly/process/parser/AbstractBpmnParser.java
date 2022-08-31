package com.alibaba.smart.framework.engine.bpmn.assembly.process.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnParser<M extends BaseElement> extends AbstractElementParser<M> {



    @Override
    protected void decorateChild(M model, BaseElement child, ParseContext context) {
        if(!this.parseModelChild(model, child)) {
            if ( (model instanceof ExtensionElementContainer) && (child instanceof ExtensionElements) ) {
                ((ExtensionElementContainer) model).setExtensionElements((ExtensionElements)child);
            }


        }
    }


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

    protected Map<String, String> parseExtendedProperties(XMLStreamReader reader, ParseContext context) {
        return XmlParseUtil.parseExtendedProperties(reader, context);
    }
}

package com.alibaba.smart.framework.engine.bpmn.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.extension.ExtensionElementsImpl;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

/**
 * Extension Elements Parser Created by ettear on 16-4-14.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ExtensionElementsImpl.class)
public class ExtensionElementsParser extends AbstractElementParser<ExtensionElementsImpl>   {

    @Override
    protected ExtensionElementsImpl parseModel(XMLStreamReader reader, ParseContext context) {
        return new ExtensionElementsImpl();
    }

    @Override
    protected void decorateChild(ExtensionElementsImpl extensionElements, BaseElement child) {
        if (child instanceof ExtensionDecorator) {
            extensionElements.decorate((ExtensionDecorator) child);
        } else {
            throw  new EngineException("Should be a instance of ExtensionDecorator :"+child.getClass());
        }
    }

    @Override
    public QName getQname() {
        return ExtensionElementsImpl.type;
    }

    @Override
    public Class<ExtensionElementsImpl> getModelType() {
        return ExtensionElementsImpl.class;
    }
}

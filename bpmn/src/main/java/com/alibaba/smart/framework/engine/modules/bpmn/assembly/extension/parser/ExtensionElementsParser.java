package com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

/**
 * Extension Elements Parser Created by ettear on 16-4-14.
 */
@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = ExtensionElements.class)

public class ExtensionElementsParser extends AbstractElementParser<ExtensionElements>   {



    @Override
    protected ExtensionElements parseModel(XMLStreamReader reader, ParseContext context) {
        return new ExtensionElements();
    }

    @Override
    protected void singingMagic(ExtensionElements model, BaseElement child) {
        if (child instanceof Extension) {
            model.addExtension((Extension) child);
        } else {
            throw  new EngineException("Should be a instance of Extension :"+child.getClass());
        }
    }

    // @Override
    // public void resolve(ExtensionElements model, ParseContext context) throws ResolveException {
    // if (null != model.getExtensions()) {
    // for (Extension element : model.getExtensions()) {
    // this.resolveElement(element, context);
    // }
    // }
    // model.setUnresolved(false);
    // }

    @Override
    public QName getQname() {
        return ExtensionElements.type;
    }

    @Override
    public Class<ExtensionElements> getModelType() {
        return ExtensionElements.class;
    }
}

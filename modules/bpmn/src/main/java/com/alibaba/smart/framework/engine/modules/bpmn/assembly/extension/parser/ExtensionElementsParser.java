package com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ResolveException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.common.assembly.Extension;

/**
 * Extension Elements Parser Created by ettear on 16-4-14.
 */
public class ExtensionElementsParser extends AbstractStAXArtifactParser<ExtensionElements> implements StAXArtifactParser<ExtensionElements> {

    public ExtensionElementsParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public ExtensionElements parse(XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                                XMLStreamException {
        ExtensionElements extensionElements = new ExtensionElements();

        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof Extension) {
                extensionElements.addExtension((Extension) element);
            }
        }
        return extensionElements;
    }

    @Override
    public void resolve(ExtensionElements model, ParseContext context) throws ResolveException {
        if (null != model.getExtensions()) {
            for (Extension element : model.getExtensions()) {
                this.resolveElement(element, context);
            }
        }
        model.setUnresolved(false);
    }

    @Override
    public QName getArtifactType() {
        return ExtensionElements.type;
    }

    @Override
    public Class<ExtensionElements> getModelType() {
        return ExtensionElements.class;
    }
}

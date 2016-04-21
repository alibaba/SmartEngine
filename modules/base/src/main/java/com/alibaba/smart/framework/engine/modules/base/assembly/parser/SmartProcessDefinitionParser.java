package com.alibaba.smart.framework.engine.modules.base.assembly.parser;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartProcessDefinition;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * SmartProcessDefinitionParser
 * Created by ettear on 16-4-14.
 */
public class SmartProcessDefinitionParser extends AbstractStAXArtifactParser<SmartProcessDefinition>
        implements StAXArtifactParser<SmartProcessDefinition> {

    public SmartProcessDefinitionParser(
            ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public SmartProcessDefinition parse(XMLStreamReader reader, ParseContext context)
            throws ParseException, XMLStreamException {
        SmartProcessDefinition smartProcessDefinition = new SmartProcessDefinition();
        smartProcessDefinition.setId(this.getString(reader, "id"));
        smartProcessDefinition.setVersion(this.getString(reader, "version"));
        smartProcessDefinition.setName(this.getString(reader, "name"));

        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof com.alibaba.smart.framework.engine.assembly.Process) {
                smartProcessDefinition.setProcess((com.alibaba.smart.framework.engine.assembly.Process)element);
            }
        }
        return smartProcessDefinition;
    }

    @Override
    public void resolve(SmartProcessDefinition model, ParseContext context) {

    }

    @Override
    public QName getArtifactType() {
        return SmartProcessDefinition.type;
    }

    @Override
    public Class<SmartProcessDefinition> getModelType() {
        return SmartProcessDefinition.class;
    }
}

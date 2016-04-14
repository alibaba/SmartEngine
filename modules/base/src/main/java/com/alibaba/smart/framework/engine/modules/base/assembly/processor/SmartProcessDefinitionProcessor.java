package com.alibaba.smart.framework.engine.modules.base.assembly.processor;

import com.alibaba.smart.framework.engine.assembly.*;
import com.alibaba.smart.framework.engine.assembly.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.assembly.processor.StAXArtifactProcessor;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorReadException;
import com.alibaba.smart.framework.engine.assembly.processor.impl.AbstractStAXArtifactProcessor;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartProcessDefinition;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartProcessDefinitionProcessor extends AbstractStAXArtifactProcessor
        implements StAXArtifactProcessor<SmartProcessDefinition> {

    public SmartProcessDefinitionProcessor(
            ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public SmartProcessDefinition read(XMLStreamReader reader, ProcessorContext context)
            throws ProcessorReadException, XMLStreamException {
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
    public void resolve(SmartProcessDefinition model, ProcessorContext context) {

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

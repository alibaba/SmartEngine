package com.alibaba.smart.framework.engine.modules.base.assembly.processor;

import com.alibaba.smart.framework.engine.assembly.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.assembly.processor.StAXArtifactProcessor;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorReadException;
import com.alibaba.smart.framework.engine.assembly.processor.impl.AbstractStAXArtifactProcessor;
import com.alibaba.smart.framework.engine.extensibility.AssemblyProcessorExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartSequenceFlow;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartSequenceFlowProcessor extends AbstractStAXArtifactProcessor
        implements StAXArtifactProcessor<SmartSequenceFlow> {

    public SmartSequenceFlowProcessor(
            ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public SmartSequenceFlow read(XMLStreamReader reader, ProcessorContext context)
            throws ProcessorReadException, XMLStreamException {
        SmartSequenceFlow smartSequenceFlow = new SmartSequenceFlow();
        smartSequenceFlow.setId(this.getString(reader, "id"));
        smartSequenceFlow.setSourceRef(this.getString(reader, "sourceRef"));
        smartSequenceFlow.setTargetRef(this.getString(reader, "targetRef"));

        this.skipToEndElement(reader);
        return smartSequenceFlow;
    }

    @Override
    public void resolve(SmartSequenceFlow model, ProcessorContext context) {
        model.setUnresolved(false);
    }

    @Override
    public QName getArtifactType() {
        return SmartSequenceFlow.type;
    }

    @Override
    public Class<SmartSequenceFlow> getModelType() {
        return SmartSequenceFlow.class;
    }
}

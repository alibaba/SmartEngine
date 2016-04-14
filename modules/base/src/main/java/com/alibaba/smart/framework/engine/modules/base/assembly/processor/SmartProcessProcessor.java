package com.alibaba.smart.framework.engine.modules.base.assembly.processor;

import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.assembly.processor.StAXArtifactProcessor;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorReadException;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorResolveException;
import com.alibaba.smart.framework.engine.assembly.processor.impl.AbstractStAXArtifactProcessor;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartProcess;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartSequenceFlow;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartProcessProcessor extends AbstractStAXArtifactProcessor
        implements StAXArtifactProcessor<SmartProcess> {

    public SmartProcessProcessor(
            ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public SmartProcess read(XMLStreamReader reader, ProcessorContext context)
            throws ProcessorReadException, XMLStreamException {
        SmartProcess smartProcess = new SmartProcess();
        smartProcess.setId(this.getString(reader, "id"));

        List<Base> elements=new ArrayList<>();
        while (this.nextChildElement(reader)) {
            Object element=this.readElement(reader,context);
            if(element instanceof Base){
                elements.add((Base)element);
            }
        }
        smartProcess.setElements(elements);
        return smartProcess;
    }

    @Override
    public void resolve(SmartProcess model, ProcessorContext context) throws ProcessorResolveException{
        if(null!=model.getElements()){
            for(Base element:model.getElements()){
                this.resolveElement(element,context);
            }
        }
        model.setUnresolved(false);
    }

    @Override
    public QName getArtifactType() {
        return SmartProcess.type;
    }

    @Override
    public Class<SmartProcess> getModelType() {
        return SmartProcess.class;
    }
}

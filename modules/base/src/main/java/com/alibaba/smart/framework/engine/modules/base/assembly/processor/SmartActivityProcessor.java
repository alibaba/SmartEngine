package com.alibaba.smart.framework.engine.modules.base.assembly.processor;

import com.alibaba.smart.framework.engine.assembly.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.assembly.processor.StAXArtifactProcessor;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorReadException;
import com.alibaba.smart.framework.engine.assembly.processor.impl.AbstractStAXArtifactProcessor;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartActivity;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartActivityProcessor extends AbstractStAXArtifactProcessor implements StAXArtifactProcessor<SmartActivity>{

    public SmartActivityProcessor(
            ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public SmartActivity read(XMLStreamReader reader, ProcessorContext context)
            throws ProcessorReadException, XMLStreamException {
        SmartActivity smartActivity=new SmartActivity();
        smartActivity.setId(this.getString(reader, "id"));
        smartActivity.setStartActivity(this.getBoolean(reader, "start"));

        this.skipToEndElement(reader);
        return smartActivity;
    }

    @Override
    public void resolve(SmartActivity model, ProcessorContext context) {
        model.setUnresolved(false);

    }

    @Override
    public QName getArtifactType() {
        return SmartActivity.type;
    }

    @Override
    public Class<SmartActivity> getModelType() {
        return SmartActivity.class;
    }
}

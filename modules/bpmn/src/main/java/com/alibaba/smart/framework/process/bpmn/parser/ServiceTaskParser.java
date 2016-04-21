package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.task.ServiceTask;

public class ServiceTaskParser extends AbstractStAXArtifactParser<ServiceTask> implements StAXArtifactParser<ServiceTask> {

    public ServiceTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ServiceTask.type;
    }

    @Override
    public Class<ServiceTask> getModelType() {
        return ServiceTask.class;
    }

    @Override
    public ServiceTask parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(this.getString(reader, "id"));

        this.skipToEndElement(reader);
        return serviceTask;
    }

}

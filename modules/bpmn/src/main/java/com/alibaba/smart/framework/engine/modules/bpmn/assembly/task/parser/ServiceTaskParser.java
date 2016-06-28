package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;

public class ServiceTaskParser extends AbstractBpmnActivityParser<ServiceTask> implements StAXArtifactParser<ServiceTask> {

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

        this.parseChildren(serviceTask, reader, context);
        return serviceTask;
    }
}

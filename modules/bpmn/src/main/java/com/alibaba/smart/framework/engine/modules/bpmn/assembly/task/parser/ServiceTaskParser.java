package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

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
        String className = this.getString(reader, "smart:class");
        className= reader.getAttributeValue(2);
         serviceTask.setClassName( className);
         
//         for(int i = 0, n = reader.getAttributeCount(); i < n; ++i) {
//             QName name = reader.getAttributeName(i);
//             String value = reader.getAttributeValue(i);
//             System.out.println("Attribute: " + name + "=" + value);
//       }

        this.parseChildren(serviceTask, reader, context);
        return serviceTask;
    }
}

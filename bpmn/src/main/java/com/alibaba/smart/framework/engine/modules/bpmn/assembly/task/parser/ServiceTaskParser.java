package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

public class ServiceTaskParser extends AbstractBpmnActivityParser<ServiceTask>  {

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
    public ServiceTask parseModel(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(XmlParseUtil.getString(reader, "id"));
        serviceTask.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
        serviceTask.setProperties(userTaskProperties);

        return serviceTask;
    }

    @Override
    protected boolean parseModelChild(ServiceTask model, BaseElement child) {

        return false;
    }

}

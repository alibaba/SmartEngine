package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.Action;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvents;
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
    public ServiceTask parseModel(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(this.getString(reader, "id"));
        return serviceTask;
    }

    @Override
    protected void parseModelChild(ServiceTask model, BaseElement child) {
        if (child instanceof Action) {
            model.setAction((Action) child);
        }
        if (child instanceof ProcessEvents) {
            model.setEvents((ProcessEvents) child);
        }

    }


}

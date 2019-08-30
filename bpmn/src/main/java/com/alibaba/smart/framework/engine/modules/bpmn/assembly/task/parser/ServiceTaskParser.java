package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = ServiceTask.class)

public class ServiceTaskParser extends AbstractBpmnParser<ServiceTask>  {


    @Override
    public QName getQname() {
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

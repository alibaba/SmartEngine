package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

public class ReceiveTaskParser extends AbstractBpmnActivityParser<ReceiveTask>  {

    public ReceiveTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getQname() {
        return ReceiveTask.type;
    }

    @Override
    public Class<ReceiveTask> getModelType() {
        return ReceiveTask.class;
    }

    @Override
    public ReceiveTask parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        ReceiveTask receiveTask = new ReceiveTask();
        receiveTask.setId(XmlParseUtil.getString(reader, "id"));
        receiveTask.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
        receiveTask.setProperties(userTaskProperties);

        return receiveTask;
    }
}

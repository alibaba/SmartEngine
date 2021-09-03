package com.alibaba.smart.framework.engine.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.SendTask;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = SendTask.class)
/**
 * @author zilong.jiangzl 2020-07-17
 */
public class SendTaskParser extends AbstractBpmnParser<SendTask> {

    @Override
    public Class<SendTask> getModelType() {
        return SendTask.class;
    }

    @Override
    public SendTask parseModel(XMLStreamReader reader, ParseContext context) {
        SendTask sendtask = new SendTask();
        sendtask.setId(XmlParseUtil.getString(reader, "id"));
        sendtask.setName(XmlParseUtil.getString(reader, "name"));

        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
        sendtask.setProperties(userTaskProperties);

        return sendtask;
    }

    @Override
    protected boolean parseModelChild(SendTask model, BaseElement child) {

        return false;
    }

}

package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.Process;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.ProcessDefinition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = UserTask.class)

public class UserTaskParser extends AbstractBpmnParser<UserTask>  {


    @Override
    public QName getQname() {
        return UserTask.type;
    }

    @Override
    public Class<UserTask> getModelType() {
        return UserTask.class;
    }

    @Override
    public UserTask parseModel(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {

        UserTask userTask = new UserTask();
        userTask.setId(XmlParseUtil.getString(reader, "id"));
        userTask.setName(XmlParseUtil.getString(reader, "name"));

        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);

        userTask.setProperties(userTaskProperties);

        return userTask;
    }

    @Override
    protected void singingMagic(UserTask model, BaseElement child) throws ParseException {
        if (child instanceof MultiInstanceLoopCharacteristics) {
            model.setMultiInstanceLoopCharacteristics((MultiInstanceLoopCharacteristics) child);
        }
    }


}

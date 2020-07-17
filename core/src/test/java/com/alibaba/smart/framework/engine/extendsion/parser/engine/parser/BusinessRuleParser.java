package com.alibaba.smart.framework.engine.extendsion.parser.engine.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extendsion.parser.engine.BusinessRuleTask;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = BusinessRuleTask.class)
public class BusinessRuleParser extends AbstractBpmnParser<BusinessRuleTask> {


    @Override
    public QName getQname() {
        return BusinessRuleTask.type;
    }

    @Override
    public Class<BusinessRuleTask> getModelType() {
        return BusinessRuleTask.class;
    }

    @Override
    public BusinessRuleTask parseModel(XMLStreamReader reader, ParseContext context) {

        BusinessRuleTask userTask = new BusinessRuleTask();
        userTask.setId(XmlParseUtil.getString(reader, "id"));
        userTask.setName(XmlParseUtil.getString(reader, "name"));

        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);

        userTask.setProperties(userTaskProperties);

        return userTask;
    }

    @Override
    protected void decorateChild(BusinessRuleTask businessRuleTask, BaseElement child) {
        if (child instanceof MultiInstanceLoopCharacteristics) {
            businessRuleTask.setMultiInstanceLoopCharacteristics((MultiInstanceLoopCharacteristics) child);
        }else{
            super.decorateChild(businessRuleTask,child);
        }
    }


}

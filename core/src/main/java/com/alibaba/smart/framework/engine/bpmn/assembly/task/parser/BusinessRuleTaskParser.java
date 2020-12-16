package com.alibaba.smart.framework.engine.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.BusinessRuleTask;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = BusinessRuleTask.class)
/**
 * @author zilong.jiangzl 2020-07-17
 */
public class BusinessRuleTaskParser extends AbstractBpmnParser<BusinessRuleTask> {


    @Override
    public Class<BusinessRuleTask> getModelType() {
        return BusinessRuleTask.class;
    }

    @Override
    public BusinessRuleTask parseModel(XMLStreamReader reader, ParseContext context) {
        BusinessRuleTask businessRuleTask = new BusinessRuleTask();
        businessRuleTask.setId(XmlParseUtil.getString(reader, "id"));
        businessRuleTask.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
        businessRuleTask.setProperties(userTaskProperties);

        return businessRuleTask;
    }

    @Override
    protected boolean parseModelChild(BusinessRuleTask model, BaseElement child) {

        return false;
    }

}

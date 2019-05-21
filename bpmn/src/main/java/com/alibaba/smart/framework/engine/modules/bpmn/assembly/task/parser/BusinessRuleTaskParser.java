package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.BusinessRuleTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 上午11:06
 */
public class BusinessRuleTaskParser extends AbstractBpmnActivityParser<BusinessRuleTask> implements StAXArtifactParser<BusinessRuleTask> {

    public BusinessRuleTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return BusinessRuleTask.type;
    }

    @Override
    public Class<BusinessRuleTask> getModelType() {
        return BusinessRuleTask.class;
    }

    @Override
    public BusinessRuleTask parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {
            BusinessRuleTask businessRuleTask = new BusinessRuleTask();
            businessRuleTask.setId(this.getString(reader, "id"));
            businessRuleTask.setName(this.getString(reader, "name"));


            Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
            businessRuleTask.setProperties(userTaskProperties);

            return businessRuleTask;
    }

}

package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

public class UserTaskParser extends AbstractBpmnActivityParser<UserTask> implements StAXArtifactParser<UserTask> {

    public UserTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return UserTask.type;
    }

    @Override
    public Class<UserTask> getModelType() {
        return UserTask.class;
    }

    @Override
    public UserTask parseModel(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {

        UserTask userTask = new UserTask();
        userTask.setId(this.getString(reader, "id"));
        userTask.setName(this.getString(reader, "name"));

        Map<String, String> userTaskProperties = getCustomMultiInstanceCandidate(reader,  context);

        userTask.setProperties(userTaskProperties);

        return userTask;
    }

    private Map<String, String> getCustomMultiInstanceCandidate(XMLStreamReader reader,ParseContext context) {

        Map<String,String> userTaskProperties = new HashMap();

        int attributeCount=reader.getAttributeCount();
        if(attributeCount>0){
            for (int i = 0; i < attributeCount; i++) {
                QName attributeName=reader.getAttributeName(i);

                String localPart = attributeName.getLocalPart();

                if("id".equals(localPart)||"name".equals(localPart)){
                    continue;
                }

                Object value=reader.getAttributeValue(attributeName.getNamespaceURI(), localPart);
                userTaskProperties.put(localPart,(String)value);


            }
        }


        return userTaskProperties;
    }

}

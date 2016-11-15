package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
    public UserTask parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        UserTask userTask = new UserTask();
        userTask.setId(this.getString(reader, "id"));
        userTask.setName(this.getString(reader, "name"));

        this.parseChildren(userTask, reader, context);
        return userTask;
    }
}

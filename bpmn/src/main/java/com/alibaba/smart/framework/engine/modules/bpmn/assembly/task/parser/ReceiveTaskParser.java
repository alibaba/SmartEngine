package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

public class ReceiveTaskParser extends AbstractBpmnActivityParser<ReceiveTask> implements StAXArtifactParser<ReceiveTask> {

    public ReceiveTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
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
        receiveTask.setId(this.getString(reader, "id"));
        return receiveTask;
    }
}

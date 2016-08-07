package com.alibaba.smart.framework.engine.modules.bpmn.assembly.executionlistener.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.executionlistener.ExecutionListener;
import com.alibaba.smart.framework.engine.modules.common.assembly.SmartBase;

public class ExecutionListenerParser extends AbstractStAXArtifactParser<ExecutionListener> implements StAXArtifactParser<ExecutionListener> {

    private final static QName artifactType = new QName(SmartBase.SMART_NS, "executionListener");

    public ExecutionListenerParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return artifactType;
    }

    @Override
    public Class<ExecutionListener> getModelType() {
        return ExecutionListener.class;
    }

    @Override
    public ExecutionListener parse(XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                                XMLStreamException {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.setType(this.getString(reader, "type"));
        executionListener.setClazzName(this.getString(reader, "class"));
        executionListener.setEvent(this.getString(reader, "event"));
        while (this.nextChildElement(reader)) {
            // TODO 居然不能删除
        }

        return executionListener;

    }

}
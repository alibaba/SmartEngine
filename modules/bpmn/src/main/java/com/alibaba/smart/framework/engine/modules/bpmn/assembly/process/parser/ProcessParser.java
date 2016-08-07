package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.Process;

public class ProcessParser extends AbstractStAXArtifactParser<Process> implements StAXArtifactParser<Process> {

    public ProcessParser(ExtensionPointRegistry extensionPointRegistry) {

        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return Process.type;
    }

    @Override
    public Class<Process> getModelType() {
        return Process.class;
    }

    @Override
    public Process parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {

        Process process = new Process();
        process.setId(this.getString(reader, "id"));

        List<BaseElement> elements = new ArrayList<>();
        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof BaseElement) {
                elements.add((BaseElement) element);
            }
        }
        process.setElements(elements);
        return process;
    }

}

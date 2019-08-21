package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.Process;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessParser extends AbstractElementParser<Process>  {

    public ProcessParser(ExtensionPointRegistry extensionPointRegistry) {

        super(extensionPointRegistry);
    }

    @Override
    public QName getQname() {
        return Process.type;
    }

    @Override
    public Class<Process> getModelType() {
        return Process.class;
    }

    @Override
    public Process parseElement(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {

        Process process = new Process();
        process.setId(XmlParseUtil.getString(reader, "id"));

        List<BaseElement> elements = new ArrayList<BaseElement>();
        while (XmlParseUtil.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof BaseElement) {
                elements.add((BaseElement) element);
            }
        }
        process.setElements(elements);
        return process;
    }

}

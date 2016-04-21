package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ResolveException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.Process;


public class ProcessParser extends AbstractStAXArtifactParser<Process> implements StAXArtifactParser<Process>{

    public ProcessParser(ExtensionPointRegistry extensionPointRegistry) {
        
        super(extensionPointRegistry);
        // TODO Auto-generated constructor stub
    }

    @Override
    public QName getArtifactType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<Process> getModelType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Process parse(XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                         XMLStreamException {
        // TODO Auto-generated method stub
        return null;
    }

}

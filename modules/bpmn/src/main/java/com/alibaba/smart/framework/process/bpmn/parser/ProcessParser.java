
package com.alibaba.smart.framework.process.bpmn.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.Process;

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

        List<Base> elements=new ArrayList<>();
        while (this.nextChildElement(reader)) {
            Object element=this.readElement(reader,context);
            if(element instanceof Base){
                elements.add((Base)element);
            }else{
                //TODO XX
                throw new RuntimeException("TODOXX");
            }
        }
        process.setElements(elements);
        return process;
    }

}

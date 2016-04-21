package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.activity.ProcessActivity;

/**
 * @author 高海军 帝奇 Apr 21, 2016 2:33:45 PM TODO 估计不需要
 */
public class ProcessActivityParser extends AbstractStAXArtifactParser<ProcessActivity> implements StAXArtifactParser<ProcessActivity> {

    public ProcessActivityParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
        // TODO Auto-generated constructor stub
    }

    @Override
    public QName getArtifactType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<ProcessActivity> getModelType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProcessActivity parse(XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                              XMLStreamException {
        // TODO Auto-generated method stub
        return null;
    }

}

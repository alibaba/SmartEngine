package com.alibaba.smart.framework.engine.modules.bpmn.assembly.common.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.common.Incoming;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXXmlParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXXmlParser;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class IncomingParser extends AbstractStAXXmlParser<Incoming> implements StAXXmlParser<Incoming> {


    public IncomingParser(ExtensionPointRegistry extensionPointRegistry) {

        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return Incoming.type;
    }

    @Override
    public Class<Incoming> getModelType() {
        return Incoming.class;
    }

    @Override
    public Incoming parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        // JUST SKIP
        this.skipToEndElement(reader);

        return null;
    }

}

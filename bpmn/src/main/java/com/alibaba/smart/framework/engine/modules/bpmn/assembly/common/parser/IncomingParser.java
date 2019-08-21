package com.alibaba.smart.framework.engine.modules.bpmn.assembly.common.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.common.Incoming;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class IncomingParser extends AbstractElementParser<Incoming>   {


    public IncomingParser(ExtensionPointRegistry extensionPointRegistry) {

        super(extensionPointRegistry);
    }

    @Override
    public QName getQname() {
        return Incoming.type;
    }

    @Override
    public Class<Incoming> getModelType() {
        return Incoming.class;
    }

    @Override
    public Incoming parseElement(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        // JUST SKIP
        XmlParseUtil.skipToEndElement(reader);

        return null;
    }

}

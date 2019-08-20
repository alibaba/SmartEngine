package com.alibaba.smart.framework.engine.modules.bpmn.assembly.common.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.common.Outgoing;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.ElementParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractElementParser;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class OutgoingParser extends AbstractElementParser<Outgoing> implements ElementParser<Outgoing> {


    public OutgoingParser(ExtensionPointRegistry extensionPointRegistry) {

        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return Outgoing.type;
    }

    @Override
    public Class<Outgoing> getModelType() {
        return Outgoing.class;
    }

    @Override
    public Outgoing parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        // JUST SKIP
        this.skipToEndElement(reader);

        return null;
    }

}

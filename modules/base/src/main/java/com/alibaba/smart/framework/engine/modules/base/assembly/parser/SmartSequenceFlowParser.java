package com.alibaba.smart.framework.engine.modules.base.assembly.parser;

import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartSequenceFlow;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * SmartSequenceFlowParser
 * Created by ettear on 16-4-14.
 */
public class SmartSequenceFlowParser extends AbstractStAXArtifactParser<SmartSequenceFlow>
        implements StAXArtifactParser<SmartSequenceFlow> {

    public SmartSequenceFlowParser(
            ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public SmartSequenceFlow parse(XMLStreamReader reader, ParseContext context)
            throws ParseException, XMLStreamException {
        SmartSequenceFlow smartSequenceFlow = new SmartSequenceFlow();
        smartSequenceFlow.setId(this.getString(reader, "id"));
        smartSequenceFlow.setSourceRef(this.getString(reader, "sourceRef"));
        smartSequenceFlow.setTargetRef(this.getString(reader, "targetRef"));

        this.skipToEndElement(reader);
        return smartSequenceFlow;
    }

    @Override
    public void resolve(SmartSequenceFlow model, ParseContext context) {
        model.setUnresolved(false);
    }

    @Override
    public QName getArtifactType() {
        return SmartSequenceFlow.type;
    }

    @Override
    public Class<SmartSequenceFlow> getModelType() {
        return SmartSequenceFlow.class;
    }
}

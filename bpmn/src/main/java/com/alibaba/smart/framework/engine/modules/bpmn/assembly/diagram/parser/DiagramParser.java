package com.alibaba.smart.framework.engine.modules.bpmn.assembly.diagram.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.diagram.Diagram;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class DiagramParser extends AbstractStAXArtifactParser<Diagram> implements StAXArtifactParser<Diagram> {

    public final static QName type = new QName(BpmnBase.BPMNDI_NAME_SPACE, "BPMNDiagram");

    private static final long serialVersionUID = -2660788294142169268L;

    public DiagramParser(ExtensionPointRegistry extensionPointRegistry) {

        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return Diagram.type;
    }

    @Override
    public Class<Diagram> getModelType() {
        return Diagram.class;
    }

    @Override
    public Diagram parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        // JUST SKIP
        this.skipToEndElement(reader);

        return null;
    }

}

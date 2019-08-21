package com.alibaba.smart.framework.engine.modules.bpmn.assembly.diagram.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.diagram.Diagram;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.ElementParser;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class DiagramParser extends AbstractElementParser<Diagram>   {

    public final static QName type = new QName(BpmnNameSpaceConstant.BPMNDI_NAME_SPACE, "BPMNDiagram");

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
    public Diagram parseElement(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        // JUST SKIP
        this.skipToEndElement(reader);

        return null;
    }

}

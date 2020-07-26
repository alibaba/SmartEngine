package com.alibaba.smart.framework.engine.bpmn.assembly.diagram.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.diagram.Diagram;
import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Diagram.class)

public class DiagramParser extends AbstractElementParser<Diagram>   {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.BPMNDI_NAME_SPACE, "BPMNDiagram");

    private static final long serialVersionUID = -2660788294142169268L;



    @Override
    public QName getQname() {
        return Diagram.qtype;
    }

    @Override
    public Class<Diagram> getModelType() {
        return Diagram.class;
    }

    @Override
    public Diagram parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        // JUST SKIP
        XmlParseUtil.skipToEndElement(reader);

        return null;
    }

}

package com.alibaba.smart.framework.engine.bpmn.assembly.artifacts.parser;

import com.alibaba.smart.framework.engine.bpmn.assembly.artifacts.TextAnnotation;
import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Serializable;

/**
 * documents comment
 * @author guoxing
 * @date 2020年11月24日14:35:32
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = TextAnnotation.class)
public class TextAnnotationParser extends AbstractElementParser<TextAnnotation> implements Serializable {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.BPMNDI_NAME_SPACE, "textAnnotation");

    private static final long serialVersionUID = -8418211427014370265L;

    @Override
    public Class<TextAnnotation> getModelType() {
        return TextAnnotation.class;
    }

    @Override
    public TextAnnotation parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        // JUST SKIP
        XmlParseUtil.skipToEndElement(reader);

        return null;
    }

}

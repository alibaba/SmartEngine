package com.alibaba.smart.framework.engine.bpmn.assembly.artifacts.parser;

import com.alibaba.smart.framework.engine.bpmn.assembly.artifacts.Association;
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
 * documents comment association for element
 * @author guoxing
 * @date 2020年11月24日14:34:08
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Association.class)
public class AssociationParser extends AbstractElementParser<Association> implements Serializable {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.BPMNDI_NAME_SPACE, "association");

    private static final long serialVersionUID = 5244437456902744239L;

    @Override
    public QName getQname() {
        return Association.qtype;
    }

    @Override
    public Class<Association> getModelType() {
        return Association.class;
    }

    @Override
    public Association parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        // JUST SKIP
        XmlParseUtil.skipToEndElement(reader);

        return null;
    }

}

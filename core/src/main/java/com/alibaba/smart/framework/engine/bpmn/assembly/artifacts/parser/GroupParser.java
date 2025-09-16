package com.alibaba.smart.framework.engine.bpmn.assembly.artifacts.parser;

import com.alibaba.smart.framework.engine.bpmn.assembly.artifacts.Group;
import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import java.io.Serializable;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * logic group category for elements
 *
 * @author guoxing
 * @date 2020/12/14 13:49:36
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Group.class)
public class GroupParser extends AbstractElementParser<Group> implements Serializable {

    public static final QName qtype = new QName(BpmnNameSpaceConstant.BPMNDI_NAME_SPACE, "group");

    private static final long serialVersionUID = 5244437456902744239L;

    @Override
    public Class<Group> getModelType() {
        return Group.class;
    }

    @Override
    public Group parseElement(XMLStreamReader reader, ParseContext context)
            throws XMLStreamException {
        // JUST SKIP
        XmlParseUtil.skipToEndElement(reader);

        return null;
    }
}

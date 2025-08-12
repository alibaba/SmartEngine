package com.alibaba.smart.framework.engine.bpmn.assembly.artifacts.parser;

import java.io.Serializable;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.artifacts.Category;
import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * logic group category for elements
 * @author guoxing
 * @date 2020/12/14 13:49:36
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Category.class)
public class CategoryParser extends AbstractElementParser<Category> implements Serializable {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.BPMNDI_NAME_SPACE, "category");

    private static final long serialVersionUID = 5244437456902744239L;

    @Override
    public Class<Category> getModelType() {
        return Category.class;
    }

    @Override
    public Category parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        // JUST SKIP
        XmlParseUtil.skipToEndElement(reader);

        return null;
    }

}

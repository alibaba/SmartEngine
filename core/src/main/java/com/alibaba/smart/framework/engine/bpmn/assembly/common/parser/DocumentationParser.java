package com.alibaba.smart.framework.engine.bpmn.assembly.common.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.common.Documentation;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Documentation.class)

public class DocumentationParser extends AbstractElementParser<Documentation>   {

    @Override
    public Class<Documentation> getModelType() {
        return Documentation.class;
    }

    @Override
    public Documentation parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        // JUST SKIP
        XmlParseUtil.skipToEndElement(reader);

        return null;
    }

}

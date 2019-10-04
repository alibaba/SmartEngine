package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartProcess;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartProcessDefinition;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = SmartProcessDefinition.class)

public class SmartProcessDefinitionParser extends AbstractElementParser<SmartProcessDefinition> {


    @Override
    protected SmartProcessDefinition parseModel(XMLStreamReader reader, ParseContext context) {
        SmartProcessDefinition smartProcessDefinition = new SmartProcessDefinition();
        smartProcessDefinition.setId(XmlParseUtil.getString(reader, "id"));
        smartProcessDefinition.setVersion(XmlParseUtil.getString(reader, "version"));
        smartProcessDefinition.setName(XmlParseUtil.getString(reader, "name"));
        return smartProcessDefinition;
    }

    @Override
    protected void singingMagic(SmartProcessDefinition smartProcessDefinition, BaseElement child) {
        if (child instanceof SmartProcess) {
            smartProcessDefinition.setProcess((SmartProcess)child);
        }
    }

    @Override
    public QName getQname() {
        return SmartProcessDefinition.type;
    }

    @Override
    public Class<SmartProcessDefinition> getModelType() {
        return SmartProcessDefinition.class;
    }
}

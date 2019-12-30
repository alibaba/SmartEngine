package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Properties;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Value;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

/**
 * Extension Elements Parser Created by ettear on 16-4-14.
 */

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Properties.class)

public class PropertiesParser extends AbstractElementParser<Properties> {



    @Override
    protected Properties parseModel(XMLStreamReader reader, ParseContext context) {
        return new Properties();
    }

    @Override
    protected void decorateChild(Properties properties, BaseElement child) {
        if (child instanceof Extension) {
            properties.getExtensionList().add((Value)child);
        }
    }

    @Override
    public QName getQname() {
        return Properties.type;
    }

    @Override
    public Class<Properties> getModelType() {
        return Properties.class;
    }
}

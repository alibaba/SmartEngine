package com.alibaba.smart.framework.engine.smart.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.smart.Properties;
import com.alibaba.smart.framework.engine.smart.PropertiesElementMarker;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

import java.util.List;

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
        if (child instanceof PropertiesElementMarker) {
            properties.getExtensionList().add((PropertiesElementMarker)child);
        }else{
            throw  new EngineException("Should be a instance of PropertiesElementMarker:" + child.getClass());
        }
    }

    @Override
    public QName getQname() {
        return null;
    }

    @Override
    public List<QName> getQnames() {
        return Properties.qtypes;
    }

    @Override
    public Class<Properties> getModelType() {
        return Properties.class;
    }
}

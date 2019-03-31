package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Properties;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Value;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractElementParser;

/**
 * Extension Elements Parser Created by ettear on 16-4-14.
 */
public class PropertiesParser extends AbstractElementParser<Properties> {

    public PropertiesParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected Properties parseModel(XMLStreamReader reader, ParseContext context) {
        return new Properties();
    }

    @Override
    protected void parseChild(Properties properties, BaseElement child) {
        if (child instanceof Extension) {
            properties.getExtensionList().add((Extension)child);
        }
    }

    @Override
    public QName getArtifactType() {
        return Properties.type;
    }

    @Override
    public Class<Properties> getModelType() {
        return Properties.class;
    }
}

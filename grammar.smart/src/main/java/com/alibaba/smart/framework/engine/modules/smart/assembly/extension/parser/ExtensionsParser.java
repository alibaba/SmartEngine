package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Extensions;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * Extension Elements Parser Created by ettear on 16-4-14.
 */
public class ExtensionsParser extends AbstractElementParser<Extensions> {

    public ExtensionsParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected Extensions parseModel(XMLStreamReader reader, ParseContext context) {
        return new Extensions();
    }

    @Override
    protected void parseSingleChild(Extensions extensions, BaseElement child) {
        if (child instanceof Extension) {
            extensions.addExtension((Extension)child);
        }
    }

    @Override
    public QName getQname() {
        return Extensions.type;
    }

    @Override
    public Class<Extensions> getModelType() {
        return Extensions.class;
    }
}

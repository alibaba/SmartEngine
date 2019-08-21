package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
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
public class SmartProcessDefinitionParser extends AbstractElementParser<SmartProcessDefinition> {

    public SmartProcessDefinitionParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected SmartProcessDefinition parseModel(XMLStreamReader reader, ParseContext context) {
        SmartProcessDefinition smartProcessDefinition = new SmartProcessDefinition();
        smartProcessDefinition.setId(XmlParseUtil.getString(reader, "id"));
        smartProcessDefinition.setVersion(XmlParseUtil.getString(reader, "version"));
        smartProcessDefinition.setName(XmlParseUtil.getString(reader, "name"));
        return smartProcessDefinition;
    }

    @Override
    protected void parseChild(SmartProcessDefinition smartProcessDefinition, BaseElement child) {
        if (child instanceof SmartProcess) {
            smartProcessDefinition.setProcess((SmartProcess)child);
        }
    }

    @Override
    public QName getArtifactType() {
        return SmartProcessDefinition.type;
    }

    @Override
    public Class<SmartProcessDefinition> getModelType() {
        return SmartProcessDefinition.class;
    }
}

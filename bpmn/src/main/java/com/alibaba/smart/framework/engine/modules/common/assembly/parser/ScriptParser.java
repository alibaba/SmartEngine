package com.alibaba.smart.framework.engine.modules.common.assembly.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.Script;
import com.alibaba.smart.framework.engine.model.assembly.SmartBase;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ScriptParser extends AbstractStAXArtifactParser<Script> implements StAXArtifactParser<Script> {

    private final static QName artifactType = new QName(SmartBase.SMART_NS, "script");

    public ScriptParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return artifactType;
    }

    @Override
    public Class<Script> getModelType() {
        return Script.class;
    }

    @Override
    public Script parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        Script script = new Script();
        script.setType(getString(reader, "type"));
        script.setResultVariable(getString(reader, "resultVariable"));
        script.setContent(reader.getElementText());
        return script;

    }

}

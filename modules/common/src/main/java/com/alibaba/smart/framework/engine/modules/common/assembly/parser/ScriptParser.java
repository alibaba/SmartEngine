package com.alibaba.smart.framework.engine.modules.common.assembly.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.Script;
import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.common.assembly.SmartBase;

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

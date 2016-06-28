package com.alibaba.smart.framework.engine.extensibility.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.assembly.parser.ArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.AssemblyParserExtensionPoint;
import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.StAXAttributeParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ResolveException;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;

/**
 * 默认处理器扩展点 Created by ettear on 16-4-12.
 */
public class DefaultAssemblyParserExtensionPoint extends AbstractPropertiesExtensionPoint implements AssemblyParserExtensionPoint {

    /**
     * Artifact处理器
     */
    private Map<QName, StAXArtifactParser>  artifactParsers        = new ConcurrentHashMap<>();
    /**
     * Artifact处理器
     */
    private Map<QName, StAXAttributeParser> attributeParsers       = new ConcurrentHashMap<>();

    private Map<Class, ArtifactParser>      resolveArtifactParsers = new ConcurrentHashMap<>();

    public DefaultAssemblyParserExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void start() {
        for (StAXArtifactParser stAXArtifactParser : artifactParsers.values()) {
            stAXArtifactParser.start();
        }
        for (StAXAttributeParser stAXAttributeParser : attributeParsers.values()) {
            stAXAttributeParser.start();
        }
    }

    @Override
    public void stop() {
        for (StAXArtifactParser stAXArtifactParser : artifactParsers.values()) {
            stAXArtifactParser.stop();
        }
        for (StAXAttributeParser stAXAttributeParser : attributeParsers.values()) {
            stAXAttributeParser.stop();
        }
    }

    @Override
    protected void initExtension(ClassLoader classLoader, String type, Object artifactParseObject)
                                                                                                  throws ExtensionPointLoadException {
        if (artifactParseObject instanceof StAXArtifactParser) {
            StAXArtifactParser artifactParser = (StAXArtifactParser) artifactParseObject;
            QName artifactType = artifactParser.getArtifactType();
            this.artifactParsers.put(artifactType, artifactParser);
            this.resolveArtifactParsers.put(artifactParser.getModelType(), artifactParser);
        } else if (artifactParseObject instanceof StAXAttributeParser) {
            StAXAttributeParser artifactParser = (StAXAttributeParser) artifactParseObject;
            this.attributeParsers.put(artifactParser.getArtifactType(), artifactParser);
            this.resolveArtifactParsers.put(artifactParser.getModelType(), artifactParser);
        }
    }

    @Override
    protected String getExtensionName() {
        return "assembly-parser";
    }

    @Override
    public Object parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        QName type = reader.getName();
        StAXArtifactParser artifactParser = this.artifactParsers.get(type);
        if (null != artifactParser) {
            return artifactParser.parse(reader, context);
        } else {
            // TODO XXX
            throw new RuntimeException("TODO");
        }
    }

    @Override
    public Object readAttribute(QName attributeName, XMLStreamReader reader, ParseContext context)
                                                                                                  throws ParseException,
                                                                                                  XMLStreamException {
        if (null == attributeName) {
            return null;
        }
        QName type = reader.getName();
        StAXAttributeParser artifactParser = this.attributeParsers.get(attributeName);
        if (null == artifactParser) {
            artifactParser = this.attributeParsers.get(type);
        }
        if (null != artifactParser) {
            return artifactParser.parse(attributeName, reader, context);
        } else if (StringUtils.equals(type.getNamespaceURI(), attributeName.getNamespaceURI())) {
            return reader.getAttributeValue(attributeName.getNamespaceURI(), attributeName.getLocalPart());
        } else {
            return null;
        }
    }

    @Override
    public void resolve(Object model, ParseContext context) throws ResolveException {
        ArtifactParser artifactParser = this.resolveArtifactParsers.get(model.getClass());
        if (null != artifactParser) {
            artifactParser.resolve(model, context);
        }
    }
}

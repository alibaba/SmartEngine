package com.alibaba.smart.framework.engine.extensionpoint.impl;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.xml.parser.*;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.exception.ResolveException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认处理器扩展点 Created by ettear on 16-4-12.
 */
@SuppressWarnings("rawtypes")
public class DefaultAssemblyParserExtensionPoint extends AbstractPropertiesExtensionPointRegistry implements AssemblyParserExtensionPoint {

    private Map<QName, StAXArtifactParser> artifactParsers = new ConcurrentHashMap<QName, StAXArtifactParser>();

    private Map<QName, StAXAttributeParser> attributeParsers = new ConcurrentHashMap<QName, StAXAttributeParser>();

    private Map<Class, ArtifactParser> resolveArtifactParsers = new ConcurrentHashMap<Class, ArtifactParser>();

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
    protected void initExtension(ClassLoader classLoader, String extensionEntryKey, Object artifactParseObject) {
        if (artifactParseObject instanceof StAXArtifactParser) {
            StAXArtifactParser artifactParser = (StAXArtifactParser) artifactParseObject;
            QName artifactType = artifactParser.getArtifactType();
            this.artifactParsers.put(artifactType, artifactParser);
            this.resolveArtifactParsers.put(artifactParser.getModelType(), artifactParser);
        }
        if (artifactParseObject instanceof StAXAttributeParser) {
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
            throw new RuntimeException("No StAXArtifactParser found for QName: " + type);
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

        QName attributeType;
        if(null==attributeName.getNamespaceURI() || "".equals(attributeName.getNamespaceURI())){
            attributeType=new QName(type.getNamespaceURI(),attributeName.getLocalPart());
        }else{
            attributeType=attributeName;
        }
        StAXAttributeParser artifactParser = this.attributeParsers.get(attributeType);
        if (null == artifactParser) {
            artifactParser = this.attributeParsers.get(type);
        }
        if (null != artifactParser) {
            return artifactParser.parse(attributeName, reader, context);
        } else if (StringUtil.equals(type.getNamespaceURI(), attributeName.getNamespaceURI())) {
            return reader.getAttributeValue(attributeName.getNamespaceURI(), attributeName.getLocalPart());
        } else {
            return null; //TODO XXX
            //throw new RuntimeException("No artifactParser found for QName: " + type);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void resolve(Object model, ParseContext context) throws ResolveException {
        ArtifactParser artifactParser = this.resolveArtifactParsers.get(model.getClass());
        if (null != artifactParser) {
            artifactParser.resolve(model, context);
        }
    }





}

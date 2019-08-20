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

    private Map<QName, StAXXmlParser> artifactParsers = new ConcurrentHashMap<QName, StAXXmlParser>();

    private Map<QName, StAXAttributeParser> attributeParsers = new ConcurrentHashMap<QName, StAXAttributeParser>();

    public DefaultAssemblyParserExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void start() {

        for (StAXXmlParser stAXArtifactParser : artifactParsers.values()) {
            stAXArtifactParser.start();
        }
        for (StAXAttributeParser stAXAttributeParser : attributeParsers.values()) {
            stAXAttributeParser.start();
        }

    }

    @Override
    public void stop() {
        for (StAXXmlParser stAXArtifactParser : artifactParsers.values()) {
            stAXArtifactParser.stop();
        }
        for (StAXAttributeParser stAXAttributeParser : attributeParsers.values()) {
            stAXAttributeParser.stop();
        }
    }

    @Override
    protected void initExtension(ClassLoader classLoader, String extensionEntryKey, Object artifactParseObject) {
        if (artifactParseObject instanceof StAXXmlParser) {
            StAXXmlParser artifactParser = (StAXXmlParser) artifactParseObject;
            QName artifactType = artifactParser.getArtifactType();
            this.artifactParsers.put(artifactType, artifactParser);
            //this.resolveArtifactParsers.put(artifactParser.getModelType(), artifactParser);
        }
        if (artifactParseObject instanceof StAXAttributeParser) {
            StAXAttributeParser artifactParser = (StAXAttributeParser) artifactParseObject;
            this.attributeParsers.put(artifactParser.getArtifactType(), artifactParser);
            //this.resolveArtifactParsers.put(artifactParser.getModelType(), artifactParser);
        }
    }

    @Override
    protected String getExtensionName() {
        return "assembly-parser";
    }

    @Override
    public Object parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        QName nodeQname = reader.getName();
        StAXXmlParser artifactParser = this.artifactParsers.get(nodeQname);
        if (null != artifactParser) {
            return artifactParser.parse(reader, context);
        } else {
            throw new RuntimeException("No StAXXmlParser found for QName: " + nodeQname);
        }
    }

    @Override
    public Object readAttribute(QName attributeName, XMLStreamReader reader, ParseContext context)
            throws ParseException,
            XMLStreamException {
        if (null == attributeName) {
            return null;
        }
        QName currentNode = reader.getName();

        QName attributeQname;
        String namespaceURI = attributeName.getNamespaceURI();
        String localPart = attributeName.getLocalPart();
        String currentNodeNamespaceURI = currentNode.getNamespaceURI();
        if(StringUtil.isEmpty(namespaceURI)){
            attributeQname=new QName(currentNodeNamespaceURI, localPart);
        }else{
            attributeQname=attributeName;
        }
        StAXAttributeParser attributeParser = this.attributeParsers.get(attributeQname);
        if (null == attributeParser) {
            attributeParser = this.attributeParsers.get(currentNode);
        }
        if (null != attributeParser) {
            return attributeParser.parse(attributeName, reader, context);
        } else if (StringUtil.equals(currentNodeNamespaceURI, namespaceURI)) {
            return reader.getAttributeValue(namespaceURI, localPart);
        } else {
            return null; //TODO XXX
            //throw new RuntimeException("No attributeParser found for QName: " + currentNode);
        }
    }


}

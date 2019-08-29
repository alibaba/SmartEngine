package com.alibaba.smart.framework.engine.extensionpoint.impl;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.xml.parser.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认处理器扩展点 Created by ettear on 16-4-12.
 */
@SuppressWarnings("rawtypes")
@ExtensionBinding(type = ExtensionConstant.EXTENSION_POINT,binding = XmlParserExtensionPoint.class)
public class DefaultXmlParserExtensionPoint  implements
    XmlParserExtensionPoint {

    private Map<QName, ElementParser> artifactParsers = new ConcurrentHashMap<QName, ElementParser>();

    private Map<QName, AttributeParser> attributeParsers = new ConcurrentHashMap<QName, AttributeParser>();

    //public DefaultXmlParserExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
    //    super(extensionPointRegistry);
    //}

    @Override
    public void start() {

        for (ElementParser stAXArtifactParser : artifactParsers.values()) {
            stAXArtifactParser.start();
        }
        for (AttributeParser attributeParser : attributeParsers.values()) {
            attributeParser.start();
        }

    }

    @Override
    public void stop() {
        for (ElementParser stAXArtifactParser : artifactParsers.values()) {
            stAXArtifactParser.stop();
        }
        for (AttributeParser attributeParser : attributeParsers.values()) {
            attributeParser.stop();
        }
    }

    //@Override
    //protected void initExtension(ClassLoader classLoader, String extensionEntryKey, Object artifactParseObject) {
    //    if (artifactParseObject instanceof ElementParser) {
    //        ElementParser artifactParser = (ElementParser) artifactParseObject;
    //        QName artifactType = artifactParser.getQname();
    //        this.artifactParsers.put(artifactType, artifactParser);
    //        //this.resolveArtifactParsers.put(artifactParser.getModelType(), artifactParser);
    //    }
    //    if (artifactParseObject instanceof AttributeParser) {
    //        AttributeParser artifactParser = (AttributeParser) artifactParseObject;
    //        this.attributeParsers.put(artifactParser.getQname(), artifactParser);
    //        //this.resolveArtifactParsers.put(artifactParser.getModelType(), artifactParser);
    //    }
    //}
    //
    //@Override
    //protected String getExtensionName() {
    //    return "assembly-parser";
    //}

    @Override
    public Object parseElement(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        QName nodeQname = reader.getName();

        //FIXME cache
        Map<String, Class> bindings = SimpleAnnotationScanner.getMap().get(
            ExtensionConstant.ELEMENT_PARSER).getBindings();
        Set<Entry<String, Class>> entries = bindings.entrySet();
        QName qName = null;
        for (Entry<String, Class> entry : entries) {
            String key = entry.getKey();
            Class<?> aClass = null;
            try {
                aClass = Class.forName(key);
                Object o = aClass.newInstance();
                Field type = aClass.getField("type");
                qName=   (QName)type.get(o);
                if(nodeQname.equals( qName)){
                    ElementParser artifactParser = (ElementParser)  entry.getValue().newInstance();
                    return artifactParser.parseElement(reader, context);

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("No ElementParser found for QName: " + nodeQname);


        //ElementParser artifactParser = this.artifactParsers.get(nodeQname);
        //if (null != artifactParser) {
        //    return artifactParser.parseElement(reader, context);
        //} else {
        //    throw new RuntimeException("No ElementParser found for QName: " + nodeQname);
        //}
    }

    @Override
    public Object parseAttribute(QName attributeQName, XMLStreamReader reader, ParseContext context)
            throws ParseException,
            XMLStreamException {
        if (null == attributeQName) {
            return null;
        }
        QName currentNode = reader.getName();
        String currentNodeNamespaceURI = currentNode.getNamespaceURI();


        QName tunedAttributeQname;

        String attributeNamespaceURI = attributeQName.getNamespaceURI();
        String attributeLocalPart = attributeQName.getLocalPart();

        if(StringUtil.isEmpty(attributeNamespaceURI)){
            tunedAttributeQname=new QName(currentNodeNamespaceURI, attributeLocalPart);
        }else{
            tunedAttributeQname=attributeQName;
        }

        AttributeParser attributeParser = this.attributeParsers.get(tunedAttributeQname);
        if (null == attributeParser) {
            attributeParser = this.attributeParsers.get(currentNode);
        }

        if (null != attributeParser) {
            return attributeParser.parseAttribute(attributeQName, reader, context);
        } else if (StringUtil.equals(currentNodeNamespaceURI, attributeNamespaceURI)) {
            return reader.getAttributeValue(attributeNamespaceURI, attributeLocalPart);
        } else {
            return null;
        }
    }


}

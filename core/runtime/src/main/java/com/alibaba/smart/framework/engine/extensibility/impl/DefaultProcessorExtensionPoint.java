package com.alibaba.smart.framework.engine.extensibility.impl;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.ProcessorExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;
import com.alibaba.smart.framework.engine.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.processor.StAXArtifactProcessor;
import com.alibaba.smart.framework.engine.processor.StAXAttributeProcessor;
import com.alibaba.smart.framework.engine.processor.exception.ProcessorReadException;
import org.apache.commons.lang3.StringUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认处理器扩展点
 * Created by ettear on 16-4-12.
 */
public class DefaultProcessorExtensionPoint extends AbstractPropertiesExtensionPoint
        implements ProcessorExtensionPoint {

    /**
     * Artifact处理器
     */
    private Map<QName, StAXArtifactProcessor>  artifactProcessors  = new ConcurrentHashMap<>();
    /**
     * Artifact处理器
     */
    private Map<QName, StAXAttributeProcessor> attributeProcessors = new ConcurrentHashMap<>();

    public DefaultProcessorExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected void initExtension(ClassLoader classLoader, String type, Object artifactProcessorObject)
            throws ExtensionPointLoadException {
        if (artifactProcessorObject instanceof StAXArtifactProcessor) {
            StAXArtifactProcessor artifactProcessor = (StAXArtifactProcessor) artifactProcessorObject;
            this.artifactProcessors.put(artifactProcessor.getArtifactType(), artifactProcessor);
        } else if (artifactProcessorObject instanceof StAXAttributeProcessor) {
            StAXAttributeProcessor artifactProcessor = (StAXAttributeProcessor) artifactProcessorObject;
            this.attributeProcessors.put(artifactProcessor.getArtifactType(), artifactProcessor);
        }
    }

    @Override
    protected String getExtensionName() {
        return "processor";
    }

    @Override
    public Object read(XMLStreamReader reader, ProcessorContext context)
            throws ProcessorReadException, XMLStreamException {
        QName type = reader.getName();
        StAXArtifactProcessor artifactProcessor = this.artifactProcessors.get(type);
        if (null != artifactProcessor) {
            return artifactProcessor.read(reader, context);
        } else {
            return null;
        }
    }

    @Override
    public Object readAttribute(QName attributeName, XMLStreamReader reader, ProcessorContext context)
            throws ProcessorReadException, XMLStreamException {
        if (null == attributeName) {
            return null;
        }
        QName type = reader.getName();
        StAXAttributeProcessor artifactProcessor = this.attributeProcessors.get(attributeName);
        if (null == artifactProcessor) {
            artifactProcessor = this.attributeProcessors.get(type);
        }
        if (null != artifactProcessor) {
            return artifactProcessor.read(attributeName, reader, context);
        } else if (StringUtils.equals(type.getNamespaceURI(), attributeName.getNamespaceURI())) {
            return reader.getAttributeValue(attributeName.getNamespaceURI(), attributeName.getLocalPart());
        } else {
            return null;
        }
    }
}

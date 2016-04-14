package com.alibaba.smart.framework.engine.extensibility.impl;

import com.alibaba.smart.framework.engine.assembly.processor.ArtifactProcessor;
import com.alibaba.smart.framework.engine.assembly.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.assembly.processor.StAXArtifactProcessor;
import com.alibaba.smart.framework.engine.assembly.processor.StAXAttributeProcessor;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorReadException;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorResolveException;
import com.alibaba.smart.framework.engine.extensibility.AssemblyProcessorExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;
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
public class DefaultAssemblyProcessorExtensionPoint extends AbstractPropertiesExtensionPoint
        implements AssemblyProcessorExtensionPoint {

    /**
     * Artifact处理器
     */
    private Map<QName, StAXArtifactProcessor>  artifactProcessors  = new ConcurrentHashMap<>();
    /**
     * Artifact处理器
     */
    private Map<QName, StAXAttributeProcessor> attributeProcessors = new ConcurrentHashMap<>();

    private Map<Class, ArtifactProcessor> resolveArtifactProcessors = new ConcurrentHashMap<>();

    public DefaultAssemblyProcessorExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected void initExtension(ClassLoader classLoader, String type, Object artifactProcessorObject)
            throws ExtensionPointLoadException {
        if (artifactProcessorObject instanceof StAXArtifactProcessor) {
            StAXArtifactProcessor artifactProcessor = (StAXArtifactProcessor) artifactProcessorObject;
            this.artifactProcessors.put(artifactProcessor.getArtifactType(), artifactProcessor);
            this.resolveArtifactProcessors.put(artifactProcessor.getModelType(), artifactProcessor);
        } else if (artifactProcessorObject instanceof StAXAttributeProcessor) {
            StAXAttributeProcessor artifactProcessor = (StAXAttributeProcessor) artifactProcessorObject;
            this.attributeProcessors.put(artifactProcessor.getArtifactType(), artifactProcessor);
            this.resolveArtifactProcessors.put(artifactProcessor.getModelType(), artifactProcessor);
        }
    }

    @Override
    protected String getExtensionName() {
        return "assembly-processor";
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

    @Override
    public void resolve(Object model, ProcessorContext context) throws ProcessorResolveException {
        ArtifactProcessor artifactProcessor = this.resolveArtifactProcessors.get(model.getClass());
        if (null != artifactProcessor) {
            artifactProcessor.resolve(model, context);
        }
    }
}

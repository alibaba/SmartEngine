package com.alibaba.smart.framework.engine.extensionpoint.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.IOUtil;

/**
 * 配置文件扩展点 Created by ettear on 16-4-12.
 */
public abstract class AbstractPropertiesExtensionPointRegistry implements ExtensionPointRegistry {

    private static final Logger    LOGGER = LoggerFactory.getLogger(AbstractPropertiesExtensionPointRegistry.class);
    /**
     * 扩展点注册器
     */
    private ExtensionPointRegistry extensionPointRegistry;

    public AbstractPropertiesExtensionPointRegistry() {
    }

    public AbstractPropertiesExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    /**
     * 扫描和加载class loader内扩展点配置
     *
     * @param classLoader ClassLoader
     */
    @Override
    public void register(String moduleName, ClassLoader classLoader)   {
        Enumeration<URL> extensionConfigFiles;
        String extensionName = getExtensionName();
        try {
            extensionConfigFiles = classLoader.getResources("smart/" + extensionName + ".properties");
        } catch (IOException e) {
            throw new EngineException("Scan config file " + extensionName + " failure!", e);
        }
        if (null != extensionConfigFiles) {
            while (extensionConfigFiles.hasMoreElements()) {
                URL extensionConfigFile = extensionConfigFiles.nextElement();
                LOGGER.debug("Load properties from the resource: " + extensionConfigFile);

                Properties properties = new Properties();
                InputStream openStream = null;
                try {
                    openStream = extensionConfigFile.openStream();
                    properties.load(openStream);
                } catch (IOException e) {
                    throw new EngineException("Load config file " + extensionConfigFile.toString()
                                                          + " failure!", e);
                } finally {
                    IOUtil.closeQuietly(openStream);
                }

                LOGGER.debug("The properties content is: " + properties);

                if (!properties.isEmpty()) {

                    for (Map.Entry<Object, Object> propertyEntry : properties.entrySet()) {
                        String entensionEntryKey = (String) propertyEntry.getKey();
                        String entensionEntryValue = (String) propertyEntry.getValue();
                        this.instantiateAndInitExtension(classLoader, entensionEntryKey, entensionEntryValue);
                    }
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void instantiateAndInitExtension(ClassLoader classLoader, String entensionEntryKey, String entensionEntryValue)
                                                                                                                 {
        Class<?> extensionValueClass;
        try {
            extensionValueClass = classLoader.loadClass(entensionEntryValue);

        } catch (ClassNotFoundException e) {
            throw new EngineException("Scan config file " + getExtensionName() + " failure!", e);
        }
        Object extensionValueObject;
        try {
            Constructor constructor = extensionValueClass.getConstructor(ExtensionPointRegistry.class);
            extensionValueObject = constructor.newInstance(this.extensionPointRegistry);
        } catch (Exception e) {
            try {
                Constructor constructor = extensionValueClass.getConstructor();
                extensionValueObject = constructor.newInstance();
            } catch (Exception ex) {
                throw new EngineException("Instance constructor for class " + entensionEntryValue + " !",
                                                      ex);
            }
        }
        this.initExtension(classLoader, entensionEntryKey, extensionValueObject);
    }

    /**
     * 初始化扩展点
     */
    protected abstract void initExtension(ClassLoader classLoader, String extensionEntryKey, Object object);

    /**
     * 扩展点名称
     *
     * @return 扩展点名称
     */
    protected abstract String getExtensionName();

    public void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }
    
    @Override
    public <T> T getExtensionPoint(Class<T> extensionPointType) {
        throw new RuntimeException("not implemented");
    }
}

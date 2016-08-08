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

import com.alibaba.smart.framework.engine.extensionpoint.ClassLoaderExtensionPoint;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensionpoint.registry.exception.ExtensionPointLoadException;
import com.alibaba.smart.framework.engine.instance.util.IOUtil;

/**
 * 配置文件扩展点 Created by ettear on 16-4-12.
 */
public abstract class AbstractPropertiesExtensionPoint implements ClassLoaderExtensionPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPropertiesExtensionPoint.class);
    /**
     * 扩展点注册器
     */
    private ExtensionPointRegistry extensionPointRegistry;

    public AbstractPropertiesExtensionPoint() {
    }

    public AbstractPropertiesExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    /**
     * 扫描和加载class loader内扩展点配置
     *
     * @param classLoader ClassLoader
     * @throws ExtensionPointLoadException
     */
    @Override
    public void load(String moduleName, ClassLoader classLoader) throws ExtensionPointLoadException {
        Enumeration<URL> extensionConfigFiles;
        String extensionName = getExtensionName();
        try {
            extensionConfigFiles = classLoader.getResources("smart/" + extensionName + ".properties");
        } catch (IOException e) {
            throw new ExtensionPointLoadException("Scan config file " + extensionName + " failure!", e);
        }
        if (null != extensionConfigFiles) {
            while (extensionConfigFiles.hasMoreElements()) {
                URL extensionConfigFile = extensionConfigFiles.nextElement();
                LOGGER.debug("Load properties from the resource: "+extensionConfigFile);
                
                Properties properties = new Properties();
                InputStream openStream = null;
                try {
                    openStream = extensionConfigFile.openStream();
                    properties.load(openStream);
                } catch (IOException e) {
                    throw new ExtensionPointLoadException("Load config file " + extensionConfigFile.toString()
                                                          + " failure!", e);
                }finally{
                    IOUtil.closeQuietly(openStream);
                }
                
                LOGGER.debug("The properties content is: "+properties);
                
                if (!properties.isEmpty()) {
                    
                    for (Map.Entry<Object, Object> propertyEntry : properties.entrySet()) {
                        String entensionEntryKey = (String) propertyEntry.getKey();
                        String entensionEntryValue = (String) propertyEntry.getValue();
                        this.initExtension(classLoader, entensionEntryKey, entensionEntryValue);
                    }
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    protected void initExtension(ClassLoader classLoader, String entensionEntryKey, String entensionEntryValue)
                                                                                        throws ExtensionPointLoadException {
        Class<?> extensionValueClass;
        try {
            extensionValueClass = classLoader.loadClass(entensionEntryValue);

        } catch (ClassNotFoundException e) {
            throw new ExtensionPointLoadException("Scan config file " + getExtensionName() + " failure!", e);
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
                throw new ExtensionPointLoadException("Instance constructor for class " + entensionEntryValue + " !", ex);
            }
        }
        this.initExtension(classLoader, entensionEntryKey, extensionValueObject);
    }

    /**
     * 初始化扩展点
     *
     * @param type Type
     * @param object Object
     * @throws ExtensionPointLoadException
     */
    protected abstract void initExtension(ClassLoader classLoader, String entensionEntryKey, Object object)
                                                                                              throws ExtensionPointLoadException;

    /**
     * 扩展点名称
     *
     * @return 扩展点名称
     */
    protected abstract String getExtensionName();

    public void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }
}

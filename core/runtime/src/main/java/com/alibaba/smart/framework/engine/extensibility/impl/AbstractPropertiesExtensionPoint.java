package com.alibaba.smart.framework.engine.extensibility.impl;

import com.alibaba.smart.framework.engine.extensibility.ClassLoaderExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件扩展点
 * Created by ettear on 16-4-12.
 */
public abstract class AbstractPropertiesExtensionPoint implements ClassLoaderExtensionPoint {

    /**
     * 扩展点注册器
     */
    private ExtensionPointRegistry extensionPointRegistry;

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
    public void load(String moduleName,ClassLoader classLoader) throws ExtensionPointLoadException {
        Enumeration<URL> extensionConfigFiles;
        try {
            extensionConfigFiles = classLoader.getResources(
                    "smart/" + getExtensionName() + ".properties");
        } catch (IOException e) {
            throw new ExtensionPointLoadException("Scan config file " + getExtensionName() + " failure!", e);
        }
        if (null != extensionConfigFiles) {
            while (extensionConfigFiles.hasMoreElements()) {
                URL extensionConfigFile = extensionConfigFiles.nextElement();
                Properties properties = new Properties();
                try {
                    properties.load(extensionConfigFile.openStream());
                } catch (IOException e) {
                    throw new ExtensionPointLoadException(
                            "Load config file " + extensionConfigFile.toString() + " failure!", e);
                }
                if (!properties.isEmpty()) {
                    for (Map.Entry<Object, Object> propertyEntry : properties.entrySet()) {
                        String type = (String) propertyEntry.getKey();
                        String className = (String) propertyEntry.getValue();
                        this.initExtension(classLoader, type, className);
                    }
                }
            }
        }
    }

    protected void initExtension(ClassLoader classLoader, String type, String className)
            throws ExtensionPointLoadException {
        Class<?> artifactProcessorClass;
        try {
            artifactProcessorClass = classLoader.loadClass(className);

        } catch (ClassNotFoundException e) {
            throw new ExtensionPointLoadException("Scan config file " + getExtensionName() + " failure!", e);
        }
        Object object;
        try {
            Constructor constructor = artifactProcessorClass.getConstructor(ExtensionPointRegistry.class);
            object = constructor.newInstance(this.extensionPointRegistry);
        } catch (Exception e) {
            try {
                Constructor constructor = artifactProcessorClass.getConstructor();
                object = constructor.newInstance();
            } catch (Exception ex) {
                throw new ExtensionPointLoadException("Instance constructor for class " + className + " !", ex);
            }
        }
        this.initExtension(classLoader, type, object);
    }

    /**
     * 初始化扩展点
     *
     * @param type   Type
     * @param object Object
     * @throws ExtensionPointLoadException
     */
    protected abstract void initExtension(ClassLoader classLoader, String type, Object object)
            throws ExtensionPointLoadException;

    /**
     * 扩展点名称
     *
     * @return 扩展点名称
     */
    protected abstract String getExtensionName();
}

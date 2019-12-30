//package com.alibaba.smart.framework.engine.extensionpoint.impl;
//
//import java.util.Map;
//
//import com.alibaba.smart.framework.engine.common.util.MapUtil;
//import com.alibaba.smart.framework.engine.exception.EngineException;
//import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
//import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
//import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
//
//
//@SuppressWarnings("rawtypes")
//@ExtensionBinding(group = ExtensionConstant.EXTENSION_POINT, bindKey = PersisterFactoryExtensionPoint.class)
//public class DefaultPersisterFactoryExtensionPoint extends AbstractPropertiesExtensionPointRegistry implements PersisterFactoryExtensionPoint {
//
//
//    private Map<Class<?>, Object> factories = MapUtil.newHashMap();
//
//
//    @Override
//    protected void initExtension(ClassLoader classLoader, String type, Object providerFactoryObject) {
//        Class<?> extensionValueClass;
//        try {
//            extensionValueClass = classLoader.loadClass(type);
//
//        } catch (ClassNotFoundException e) {
//            throw new EngineException("Scan config file " + getExtensionName() + " failure!", e);
//        }
//        this.factories.put(extensionValueClass, providerFactoryObject);
//    }
//
//    @Override
//    protected String getExtensionName() {
//        return "persister-extensions";
//    }
//
//
//    @Override
//    public <T> T getExtensionPoint(Class<T> modelType) {
//        return (T) this.factories.get(modelType);
//    }
//
//
//    @Override
//    public void start() {
//
//    }
//
//    @Override
//    public void stop() {
//
//    }
//}

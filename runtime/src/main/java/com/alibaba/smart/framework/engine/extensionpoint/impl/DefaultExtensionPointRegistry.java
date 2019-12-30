//package com.alibaba.smart.framework.engine.extensionpoint.impl;
//
//import java.util.Map;
//
//import com.alibaba.smart.framework.engine.SmartEngine;
//import com.alibaba.smart.framework.engine.common.util.MapUtil;
//import com.alibaba.smart.framework.engine.exception.EngineException;
//import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * 默认扩展注册器实现 Created by ettear on 16-4-12.
// */
//public class DefaultExtensionPointRegistry extends AbstractPropertiesExtensionPointRegistry {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExtensionPointRegistry.class);
//
//    private Map<Class<?>, Object> extensionPoints = MapUtil.newHashMap();
//
//    public DefaultExtensionPointRegistry(SmartEngine engine) {
//        super.setExtensionPointRegistry(this);
//        this.extensionPoints.put(SmartEngine.class, engine);
//    }
//
//    @Override
//    public void register() {
//        //一层加载:将extensions properties全部加载进来,加载一层扩展点
//        //TODO extensionPoints 这个里面的东西还是比较杂,还可以细化. 太多的instanceof 是有点懒政的.
//        super.register();
//        for (Object extensionPoint : extensionPoints.values()) {
//            if (extensionPoint instanceof ExtensionPointRegistry) {
//                LOGGER.debug(extensionPoint.getClass() + " is a ExtensionPointRegistry,so deep into.");
//
//                //两层加载:找到扩展点的扩展点,然后继续加载
//                ExtensionPointRegistry extensionPointRegistry = (ExtensionPointRegistry) extensionPoint;
//                extensionPointRegistry.register();
//            } else {
//                LOGGER.debug(extensionPoint.getClass() + " is not a ExtensionPointRegistry,so ignored.");
//            }
//        }
//    }
//
//    @Override
//    public <T> T getExtensionPoint(Class<T> extensionPointType) {
//        return (T) this.extensionPoints.get(extensionPointType);
//    }
//
//    @Override
//    public void start() {
//        for (Object extensionPoint : extensionPoints.values()) {
//            if (extensionPoint instanceof LifeCycleHook) {
//                LOGGER.debug(extensionPoint.getClass() + " is a LifeCycleHook,so deep into.");
//
//                LifeCycleHook lifeCycleHook = (LifeCycleHook) extensionPoint;
//                lifeCycleHook.start();
//            } else {
//                LOGGER.debug(extensionPoint.getClass() + " is not a LifeCycleHook,so ignored.");
//
//            }
//        }
//    }
//
//    @Override
//    public void stop() {
//        for (Object extensionPoint : extensionPoints.values()) {
//            if (extensionPoint instanceof LifeCycleHook) {
//                LifeCycleHook lifeCycleHook = (LifeCycleHook) extensionPoint;
//                lifeCycleHook.stop();
//            }
//        }
//    }
//
//    @SuppressWarnings("rawtypes")
//    @Override
//    protected void initExtension(ClassLoader classLoader, String type, Object object) {
//        Class interfaceClazz;
//        try {
//            interfaceClazz = classLoader.loadClass(type);
//        } catch (ClassNotFoundException e) {
//            throw new EngineException("Class[" + type + "] not found!", e);
//        }
//        this.extensionPoints.put(interfaceClazz, object);
//    }
//
//    @Override
//    protected String getExtensionName() {
//        return "extensions";
//    }
//}

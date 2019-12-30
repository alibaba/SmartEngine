//package com.alibaba.smart.framework.engine.retry.extensionpoint.impl;
//
//import java.util.Map;
//
//import com.alibaba.smart.framework.engine.common.util.MapUtil;
//import com.alibaba.smart.framework.engine.exception.EngineException;
//import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
//import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
//import com.alibaba.smart.framework.engine.extensionpoint.impl.AbstractPropertiesExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.retry.RetryExtensionPoint;
//
///**
// * @author zhenhong.tzh
// * @date 2019-04-27
// */
//@ExtensionBinding(group = ExtensionConstant.EXTENSION_POINT, bindKey = RetryExtensionPoint.class)
//
//public class DefaultRetryExtensionPoint extends AbstractPropertiesExtensionPointRegistry
//    implements RetryExtensionPoint {
//
//    private Map<Class<?>, Object> retryFactories = MapUtil.newHashMap();
//
//
//    @Override
//    public void start() {
//    }
//
//    @Override
//    public void stop() {
//    }
//
//    @Override
//    protected void initExtension(ClassLoader classLoader, String key, Object object) {
//        Class<?> extensionValueClass;
//        try {
//            extensionValueClass = classLoader.loadClass(key);
//        } catch (ClassNotFoundException e) {
//            throw new EngineException("Scan config file " + getExtensionName() + " failure!", e);
//        }
//        this.retryFactories.put(extensionValueClass, object);
//    }
//
//    @Override
//    public <T> T getExtensionPoint(Class<T> modelType) {
//        return (T)this.retryFactories.get(modelType);
//    }
//
//    @Override
//    protected String getExtensionName() {
//        return "retry-extensions";
//    }
//}

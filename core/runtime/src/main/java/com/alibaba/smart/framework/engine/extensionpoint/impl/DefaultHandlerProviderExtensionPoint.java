package com.alibaba.smart.framework.engine.extensionpoint.impl;

// package com.alibaba.smart.framework.engine.extensionpoint.registry.impl;
//
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;
//
// import com.alibaba.smart.framework.engine.assembly.Handler;
// import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
// import com.alibaba.smart.framework.engine.exception.EngineException;
// import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
// import com.alibaba.smart.framework.engine.extensionpoint.registry.HandlerProviderExtensionPoint;
// import com.alibaba.smart.framework.engine.extensionpoint.registry.exception.ExtensionPointRegistryException;
// import com.alibaba.smart.framework.engine.invocation.Invoker;
// import com.alibaba.smart.framework.engine.provider.HandlerProvider;
//
// /**
// * DefaultHandlerProviderExtensionPoint Created by ettear on 16-4-29.
// */
// @SuppressWarnings("rawtypes")
//
// public class DefaultHandlerProviderExtensionPoint extends AbstractPropertiesExtensionPointRegistry implements
// HandlerProviderExtensionPoint {
//
// private Map<Class, HandlerProvider> providers = new ConcurrentHashMap<>();
//
// public DefaultHandlerProviderExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
// super(extensionPointRegistry);
// }
//
// @SuppressWarnings("unchecked")
// @Override
// public Invoker createInvoker(Handler handler) {
// if (null != this.providers.get(handler.getClass())) {
// return this.providers.get(handler.getClass()).createInvoker(handler);
// } else {
// throw new EngineException("No invoker found for "+handler.getClass());
// }
// }
//
// @Override
// protected void initExtension(ClassLoader classLoader, String type, Object handlerProviderObject)
// throws ExtensionPointRegistryException {
// if (handlerProviderObject instanceof HandlerProvider) {
// HandlerProvider handlerProvider = (HandlerProvider) handlerProviderObject;
// this.providers.put(handlerProvider.getType(), handlerProvider);
// }
// }
//
// @Override
// public void start() {
// for (HandlerProvider provider : providers.values()) {
// if (provider instanceof LifeCycleListener) {
// ((LifeCycleListener) provider).start();
// }
// }
// }
//
// @Override
// public void stop() {
// for (HandlerProvider provider : providers.values()) {
// if (provider instanceof LifeCycleListener) {
// ((LifeCycleListener) provider).stop();
// }
// }
// }
//
// @Override
// protected String getExtensionName() {
// return "handler-provider";
// }
// }

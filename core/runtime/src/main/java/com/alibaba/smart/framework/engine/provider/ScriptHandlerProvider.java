//package com.alibaba.smart.framework.engine.provider;
//
//import com.alibaba.smart.framework.engine.assembly.Script;
//import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.invocation.Invoker;
//import com.alibaba.smart.framework.engine.script.ScriptCompilerExtensionPoint;
//
///**
// * Created by ettear on 16-4-29.
// */
//public class ScriptHandlerProvider implements HandlerProvider<Script> {
//
//    private ExtensionPointRegistry extensionPointRegistry;
//
//    public ScriptHandlerProvider(ExtensionPointRegistry extensionPointRegistry) {
//        this.extensionPointRegistry = extensionPointRegistry;
//    }
//
//    @Override
//    public Class<Script> getType() {
//        return Script.class;
//    }
//
//    @Override
//    public Invoker createInvoker(Script script) {
//        return this.extensionPointRegistry.getExtensionPoint(ScriptCompilerExtensionPoint.class).compile(script);
//    }
//}

//package com.alibaba.smart.framework.engine.modules.mvel;
//
//import com.alibaba.smart.framework.engine.invocation.Invoker;
//import com.alibaba.smart.framework.engine.model.assembly.Script;
//import com.alibaba.smart.framework.engine.script.ScriptCompiler;
//
///**
// * Created by ettear on 16-4-29.
// */
//public class MvelScriptCompiler implements ScriptCompiler {
//
//    @Override
//    public String getType() {
//        return "mvel";
//    }
//
//    @Override
//    public Invoker compile(Script script) {
//        return new MvelInvoker(script);
//    }
//}

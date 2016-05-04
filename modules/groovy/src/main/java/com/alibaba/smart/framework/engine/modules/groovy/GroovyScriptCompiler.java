package com.alibaba.smart.framework.engine.modules.groovy;

import com.alibaba.smart.framework.engine.assembly.Script;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.script.ScriptCompiler;

/**
 * Created by ettear on 16-4-29.
 */
public class GroovyScriptCompiler implements ScriptCompiler {

    @Override
    public String getType() {
        return "groovy";
    }

    @Override
    public Invoker compile(Script script) {
        return new GroovyInvoker(script);
    }
}

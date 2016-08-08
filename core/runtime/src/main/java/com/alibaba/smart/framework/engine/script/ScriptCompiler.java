package com.alibaba.smart.framework.engine.script;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.model.assembly.Script;

/**
 * Created by ettear on 16-4-29.
 */
public interface ScriptCompiler {

    String getType();

    Invoker compile(Script script);
}

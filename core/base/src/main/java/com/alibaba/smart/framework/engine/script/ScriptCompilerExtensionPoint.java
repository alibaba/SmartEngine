package com.alibaba.smart.framework.engine.script;

import com.alibaba.smart.framework.engine.invocation.Invoker;

/**
 * Created by ettear on 16-4-29.
 */
public interface ScriptCompilerExtensionPoint {
    Invoker compile(String scriptType,String script);
}

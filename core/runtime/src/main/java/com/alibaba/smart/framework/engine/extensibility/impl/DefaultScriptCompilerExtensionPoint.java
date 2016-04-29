package com.alibaba.smart.framework.engine.extensibility.impl;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.script.ScriptCompiler;
import com.alibaba.smart.framework.engine.script.ScriptCompilerExtensionPoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultScriptCompilerExtensionPoint
 * Created by ettear on 16-4-29.
 */
public class DefaultScriptCompilerExtensionPoint extends AbstractPropertiesExtensionPoint
        implements ScriptCompilerExtensionPoint, LifeCycleListener {

    private Map<String, ScriptCompiler> scriptCompilers = new ConcurrentHashMap<>();

    @Override
    public Invoker compile(String scriptType, String script) {
        ScriptCompiler scriptCompiler = this.scriptCompilers.get(scriptType);
        if (null != scriptCompiler) {
            return scriptCompiler.compile(script);
        }
        return null;
    }

    @Override
    protected void initExtension(ClassLoader classLoader, String type, Object scriptCompilerObject)
            throws ExtensionPointLoadException {
        if (scriptCompilerObject instanceof ScriptCompiler) {
            ScriptCompiler scriptCompiler = (ScriptCompiler) scriptCompilerObject;
            this.scriptCompilers.put(scriptCompiler.getType(), scriptCompiler);
        }
    }

    @Override
    public void start() {
        for (ScriptCompiler scriptCompiler : scriptCompilers.values()) {
            if (scriptCompiler instanceof LifeCycleListener) {
                ((LifeCycleListener) scriptCompiler).start();
            }
        }
    }

    @Override
    public void stop() {
        for (ScriptCompiler scriptCompiler : scriptCompilers.values()) {
            if (scriptCompiler instanceof LifeCycleListener) {
                ((LifeCycleListener) scriptCompiler).stop();
            }
        }
    }

    @Override
    protected String getExtensionName() {
        return "script-compiler";
    }
}

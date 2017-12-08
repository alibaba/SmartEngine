package com.alibaba.smart.framework.engine.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.pvm.PvmElement;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public abstract class ContextBoundedJavaDelegation implements JavaDelegation {

    @Getter
    @Setter
    protected String className;
    @Getter
    @Setter
    protected PvmElement pvmElement;
    @Getter
    @Setter
    protected ExtensionPointRegistry extensionPointRegistry;


}

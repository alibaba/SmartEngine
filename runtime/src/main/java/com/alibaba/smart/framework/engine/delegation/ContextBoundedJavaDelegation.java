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
    private String className;
    @Getter
    @Setter
    private PvmElement pvmElement;
    @Getter
    @Setter
    private ExtensionPointRegistry extensionPointRegistry;


}

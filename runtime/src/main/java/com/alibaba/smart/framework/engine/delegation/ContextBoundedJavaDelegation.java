package com.alibaba.smart.framework.engine.delegation;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.pvm.PvmElement;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 高海军 帝奇
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

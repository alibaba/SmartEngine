package com.alibaba.smart.framework.engine.delegation;

import com.alibaba.smart.framework.engine.model.assembly.Activity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 高海军 帝奇
 */
public abstract class ContextBoundedJavaDelegation implements JavaDelegation {

    @Getter @Setter protected String className;
    @Getter @Setter protected Activity activity;
}

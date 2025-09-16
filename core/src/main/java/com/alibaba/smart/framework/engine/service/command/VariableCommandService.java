package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

/**
 * 主要变量插入。
 *
 * @author 高海军 帝奇 2021.02.25
 */
public interface VariableCommandService {

    void insert(VariableInstance... variableInstance);
}

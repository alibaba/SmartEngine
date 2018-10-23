package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

public interface VariableInstanceStorage {

    void insert(VariablePersister variablePersister, VariableInstance variableInstance);

    List<VariableInstance> findList(String processInstanceId, String executionInstanceId,
                                    VariablePersister variablePersister);

}

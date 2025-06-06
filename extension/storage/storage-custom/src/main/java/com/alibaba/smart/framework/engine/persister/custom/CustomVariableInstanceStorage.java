package com.alibaba.smart.framework.engine.persister.custom;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  16:54.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = VariableInstanceStorage.class)

public class CustomVariableInstanceStorage implements VariableInstanceStorage {

    @Override
    public void insert(VariablePersister variablePersister, VariableInstance variableInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {

    }

    @Override
    public List<VariableInstance> findList(String processInstanceId, String executionInstanceId,
                                           VariablePersister variablePersister,String tenantId,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

//    @Override
//    public List<VariableInstance> findAll(String processInstanceId,VariablePersister variablePersister, ProcessEngineConfiguration processEngineConfiguration) {
//        return null;
//    }
}

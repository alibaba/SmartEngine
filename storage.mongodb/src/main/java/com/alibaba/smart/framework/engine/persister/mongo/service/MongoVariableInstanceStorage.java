package com.alibaba.smart.framework.engine.persister.mongo.service;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  21:52.
 */
public class MongoVariableInstanceStorage implements VariableInstanceStorage {


    private static final String INSTANCE = "se_variable_instance";

    @Override
    public void insert(VariablePersister variablePersister, VariableInstance variableInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {

    }

    @Override
    public List<VariableInstance> findList(String processInstanceId, String executionInstanceId,
                                           VariablePersister variablePersister,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }
}
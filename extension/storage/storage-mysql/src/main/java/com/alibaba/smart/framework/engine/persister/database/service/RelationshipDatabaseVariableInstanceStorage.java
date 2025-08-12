package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.VariableInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.VariableInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.VariableInstanceEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = VariableInstanceStorage.class)
public class RelationshipDatabaseVariableInstanceStorage implements VariableInstanceStorage {

    @Override
    public void insert(VariablePersister variablePersister, VariableInstance variableInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {
        Object value = variableInstance.getFieldValue();
        if (null == value) {
           throw new EngineException("value can not be null: "+ variableInstance);
        }

        VariableInstanceDAO variableInstanceDAO = (VariableInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("variableInstanceDAO");

        VariableInstanceEntity variableInstanceEntity = new VariableInstanceEntity();
        String variableInstanceInstanceId = variableInstance.getInstanceId();
        if(null != variableInstanceInstanceId){
            variableInstanceEntity.setId(Long.valueOf(variableInstanceInstanceId));
        }

        variableInstanceEntity.setTenantId(variableInstance.getTenantId());
        variableInstanceEntity.setExecutionInstanceId((Long.valueOf(variableInstance.getExecutionInstanceId())));
        variableInstanceEntity.setFieldKey(variableInstance.getFieldKey());
        variableInstanceEntity.setProcessInstanceId((Long.valueOf(variableInstance.getProcessInstanceId())));
        Class fieldType = variableInstance.getFieldType();

        if(null != fieldType){
            variableInstanceEntity.setFieldType(fieldType.getName());

            if (VariableInstanceBuilder.isAssignableFromString(fieldType)) {
                variableInstanceEntity.setFieldStringValue((String)value);
            } else if (VariableInstanceBuilder.isAssignableFromBoolean(fieldType)) {
                variableInstanceEntity.setFieldStringValue(String.valueOf(value));
            } else if (VariableInstanceBuilder.isAssignableFromInteger(fieldType)) {
                variableInstanceEntity.setFieldLongValue(Long.valueOf((Integer)value));
            } else if (VariableInstanceBuilder.isAssignableFromShort(fieldType)) {
                variableInstanceEntity.setFieldLongValue(Long.valueOf((Short)value));
            } else if (VariableInstanceBuilder.isAssignableFromLong(fieldType)) {
                variableInstanceEntity.setFieldLongValue((Long)value);
            } else if (VariableInstanceBuilder.isAssignableFromFloat(fieldType)) {
                variableInstanceEntity.setFieldDoubleValue(Double.valueOf((Float)value));
            } else if (VariableInstanceBuilder.isAssignableFromDouble(fieldType)) {
                variableInstanceEntity.setFieldDoubleValue((Double)value);
            } else if (byte.class.isAssignableFrom(fieldType) || byte[].class.isAssignableFrom(fieldType)) {
                throw new EngineException("NOT support byte group so far");
            } else if (char.class.isAssignableFrom(fieldType) || char[].class.isAssignableFrom(fieldType)) {
                throw new EngineException("NOT support char group so far");
            }else {
                VariableInstanceBuilder.serialize(variablePersister, value, variableInstanceEntity);
            }
        } else {
            VariableInstanceBuilder.serialize(variablePersister, value, variableInstanceEntity);
        }

        Date currentDate = DateUtil.getCurrentDate();
        variableInstanceEntity.setGmtCreate(currentDate);
        variableInstanceEntity.setGmtModified(currentDate);

        variableInstanceDAO.insert(variableInstanceEntity);

    }


    @Override
    public List<VariableInstance> findList(String processInstanceId, String executionInstanceId,
                                           VariablePersister variablePersister,String tenantId,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        VariableInstanceDAO variableInstanceDAO = (VariableInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("variableInstanceDAO");
        List<VariableInstance> variableInstanceList = null;
        Long executionInstanceId1;
        if(executionInstanceId == null){
             executionInstanceId1 = null;

        }else {
             executionInstanceId1 = Long.valueOf(executionInstanceId);

        }
        List<VariableInstanceEntity> list = variableInstanceDAO.findList(Long.valueOf(processInstanceId), executionInstanceId1,tenantId);
        if (null != list) {
            variableInstanceList = VariableInstanceBuilder.build(variablePersister, list);
        }

        return variableInstanceList;
    }


//    @Override
//    public List<VariableInstance> findAll(String processInstanceId,VariablePersister variablePersister, ProcessEngineConfiguration processEngineConfiguration) {
//        VariableInstanceDAO variableInstanceDAO = (VariableInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("variableInstanceDAO");
//        List<VariableInstance> variableInstanceList = null;
//        List<VariableInstanceEntity> list = variableInstanceDAO.findAll(  Long.valueOf(processInstanceId));
//        if (null != list) {
//            variableInstanceList = build(variablePersister, list);
//        }
//
//        return variableInstanceList;
//    }
}

package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.VariableInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.VariableInstanceEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = VariableInstanceStorage.class)
public class RelationshipDatabaseVariableInstanceStorage implements VariableInstanceStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelationshipDatabaseVariableInstanceStorage.class);

    @Override
    public void insert(VariablePersister variablePersister, VariableInstance variableInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {
        Object value = variableInstance.getFieldValue();
        if (null == value) {
            return;
        }

        VariableInstanceDAO variableInstanceDAO = (VariableInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("variableInstanceDAO");

        VariableInstanceEntity variableInstanceEntity = new VariableInstanceEntity();
        String variableInstanceInstanceId = variableInstance.getInstanceId();
        if(null != variableInstanceInstanceId){
            variableInstanceEntity.setId(Long.valueOf(variableInstanceInstanceId));
        }
        variableInstanceEntity.setExecutionInstanceId((Long.valueOf(variableInstance.getExecutionInstanceId())));
        variableInstanceEntity.setFieldKey(variableInstance.getFieldKey());
        variableInstanceEntity.setProcessInstanceId((Long.valueOf(variableInstance.getProcessInstanceId())));
        Class fieldType = variableInstance.getFieldType();
        variableInstanceEntity.setFieldType(fieldType.getName());

        if (isAssignableFromString(fieldType)) {
            variableInstanceEntity.setFieldStringValue((String)value);
        } else if (isAssignableFromBoolean(fieldType)) {
            variableInstanceEntity.setFieldStringValue(String.valueOf(value));
        } else if (isAssignableFromInteger(fieldType)) {
            variableInstanceEntity.setFieldLongValue(Long.valueOf((Integer)value));
        } else if (isAssignableFromShort(fieldType)) {
            variableInstanceEntity.setFieldLongValue(Long.valueOf((Short)value));
        } else if (isAssignableFromLong(fieldType)) {
            variableInstanceEntity.setFieldLongValue((Long)value);
        } else if (isAssignableFromFloat(fieldType)) {
            variableInstanceEntity.setFieldDoubleValue(Double.valueOf((Float)value));
        } else if (isAssignableFromDouble(fieldType)) {
            variableInstanceEntity.setFieldDoubleValue((Double)value);
        } else if (byte.class.isAssignableFrom(fieldType) || byte[].class.isAssignableFrom(fieldType)) {
            throw new EngineException("NOT support byte group so far");
        } else if (char.class.isAssignableFrom(fieldType) || char[].class.isAssignableFrom(fieldType)) {
            throw new EngineException("NOT support char group so far");
        } else {
            //ASSUME the others are all pojos.
            String serializedValue = variablePersister.serialize(value);
            variableInstanceEntity.setFieldStringValue(serializedValue);
        }

        Date currentDate = DateUtil.getCurrentDate();
        variableInstanceEntity.setGmtCreate(currentDate);
        variableInstanceEntity.setGmtModified(currentDate);

        variableInstanceDAO.insert(variableInstanceEntity);

    }

    @Override
    public List<VariableInstance> findList(String processInstanceId, String executionInstanceId,
                                           VariablePersister variablePersister,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        VariableInstanceDAO variableInstanceDAO = (VariableInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("variableInstanceDAO");
        List<VariableInstance> variableInstanceList = null;
        List<VariableInstanceEntity> list = variableInstanceDAO.findList(Long.valueOf(processInstanceId), Long.valueOf(executionInstanceId));
        if (null != list) {
            variableInstanceList = new ArrayList<VariableInstance>(list.size());
            for (VariableInstanceEntity variableInstanceEntity : list) {
                VariableInstance variableInstance = new DefaultVariableInstance();
                variableInstance.setInstanceId(variableInstanceEntity.getId().toString());
                variableInstance.setStartTime(variableInstanceEntity.getGmtCreate());
                variableInstance.setCompleteTime(variableInstanceEntity.getGmtModified());
                variableInstance.setProcessInstanceId(variableInstanceEntity.getProcessInstanceId().toString());
                variableInstance.setExecutionInstanceId(variableInstanceEntity.getExecutionInstanceId().toString());

                variableInstance.setFieldKey(variableInstanceEntity.getFieldKey());
                String fieldType1 = variableInstanceEntity.getFieldType();

                //TUNE CACHE
                try {
                    Class<?> fieldType = Class.forName(fieldType1);
                    variableInstance.setFieldType(fieldType);

                    if (isAssignableFromString(fieldType)) {
                        variableInstance.setFieldValue(variableInstanceEntity.getFieldStringValue());
                    } else if (isAssignableFromBoolean(fieldType)) {
                        variableInstance.setFieldValue(Boolean.valueOf(variableInstanceEntity.getFieldStringValue()));
                    } else if (isAssignableFromInteger(fieldType)) {
                        variableInstance.setFieldValue(variableInstanceEntity.getFieldLongValue().intValue());
                    } else if (isAssignableFromShort(fieldType)) {
                        variableInstance.setFieldValue(variableInstanceEntity.getFieldLongValue().shortValue());
                    } else if (isAssignableFromLong(fieldType)) {
                        variableInstance.setFieldValue(variableInstanceEntity.getFieldLongValue());
                    } else if (isAssignableFromFloat(fieldType)) {
                        variableInstance.setFieldValue(variableInstanceEntity.getFieldDoubleValue().floatValue());
                    } else if (isAssignableFromDouble(fieldType)) {
                        variableInstance.setFieldValue(variableInstanceEntity.getFieldDoubleValue());
                    } else {
                        //ASSUME the others are all pojos.
                        Object serializedValue =  variablePersister.deserialize(variableInstanceEntity.getFieldStringValue(),fieldType);
                        variableInstance.setFieldValue(serializedValue);
                    }

                } catch (ClassNotFoundException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                variableInstanceList.add(variableInstance);
            }
        }

        return variableInstanceList;
    }

    private boolean isAssignableFromDouble(Class<?> fieldType) {
        return double.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromLong(Class<?> fieldType) {
        return long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromInteger(Class<?> fieldType) {
        return int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromString(Class<?> fieldType) {
        return String.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromFloat(Class fieldType) {
        return float.class.isAssignableFrom(fieldType) || Float.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromShort(Class fieldType) {
        return short.class.isAssignableFrom(fieldType) || Short.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromBoolean(Class fieldType) {
        return boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType);
    }
}

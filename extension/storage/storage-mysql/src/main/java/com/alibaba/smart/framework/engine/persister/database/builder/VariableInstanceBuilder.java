package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.VariableInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.service.RelationshipDatabaseVariableInstanceStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanricheng@163.com
 * @date 2025/05/015
 */
public final class VariableInstanceBuilder {

    public static final Logger LOGGER = LoggerFactory.getLogger(RelationshipDatabaseVariableInstanceStorage.class);

    public static void serialize(VariablePersister variablePersister, Object value, VariableInstanceEntity variableInstanceEntity) {
        //ASSUME the others are all pojos.
        String serializedValue = variablePersister.serialize(value);
        variableInstanceEntity.setFieldStringValue(serializedValue);
    }


    public static List<VariableInstance> build(VariablePersister variablePersister, List<VariableInstanceEntity> list) {
        List<VariableInstance> variableInstanceList;
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
                    Object serializedValue = variablePersister.deserialize(variableInstanceEntity.getFieldKey(), variableInstanceEntity
                            .getFieldType(), variableInstanceEntity.getFieldStringValue());
                    variableInstance.setFieldValue(serializedValue);
                }

            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            }
            variableInstanceList.add(variableInstance);
        }
        return variableInstanceList;
    }

    public static boolean isAssignableFromDouble(Class<?> fieldType) {
        return double.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType);
    }

    public static boolean isAssignableFromLong(Class<?> fieldType) {
        return long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType);
    }

    public static boolean isAssignableFromInteger(Class<?> fieldType) {
        return int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType);
    }

    public static boolean isAssignableFromString(Class<?> fieldType) {
        return String.class.isAssignableFrom(fieldType);
    }

    public static boolean isAssignableFromFloat(Class fieldType) {
        return float.class.isAssignableFrom(fieldType) || Float.class.isAssignableFrom(fieldType);
    }

    public static boolean isAssignableFromShort(Class fieldType) {
        return short.class.isAssignableFrom(fieldType) || Short.class.isAssignableFrom(fieldType);
    }

    public static boolean isAssignableFromBoolean(Class fieldType) {
        return boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType);
    }
}

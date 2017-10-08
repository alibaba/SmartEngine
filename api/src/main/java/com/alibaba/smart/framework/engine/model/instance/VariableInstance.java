package com.alibaba.smart.framework.engine.model.instance;

public interface VariableInstance extends LifeCycleInstance {

    Class getFieldType();

    void setFieldType(Class fieldType);

    Long getProcessInstanceId();

    void setProcessInstanceId(Long processInstanceId);

    Long getExecutionInstanceId();

    void setExecutionInstanceId(Long executionInstanceId);

    void setFieldKey(String fieldKey);

    String getFieldKey();

    void setFieldValue(Object value);

    Object getFieldValue();

}

package com.alibaba.smart.framework.engine.model.instance;

public interface VariableInstance extends LifeCycleInstance {

    Class getFieldType();

    void setFieldType(Class fieldType);

    String getProcessInstanceId();

    void setProcessInstanceId(String processInstanceId);

    String getExecutionInstanceId();

    void setExecutionInstanceId(String executionInstanceId);

    void setFieldKey(String fieldKey);

    String getFieldKey();

    void setFieldValue(Object value);

    Object getFieldValue();

}

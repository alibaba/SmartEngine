package com.alibaba.smart.framework.engine.service.param.query.condition;

/**
 * CustomField
 * <pre>
 * 自定义字段条件
 *  </pre>
 *
 * @author xiuqun.hxq@alibaba-inc.com
 * @date 2019/3/13 下午5:03  03月 第 13天
 */
public class CustomField {


    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段值
     */
    private Object fieldValue;


    /**
     * 比较条件,如大于(>)、小于(<)、大于等于(>=)、小于等于(<=)
     */
    private String operator;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}

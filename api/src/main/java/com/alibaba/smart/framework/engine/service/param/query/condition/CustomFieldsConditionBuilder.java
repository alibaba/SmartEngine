package com.alibaba.smart.framework.engine.service.param.query.condition;

/**
 * CustomFieldsConditionBuilder
 * <pre>
 * 自定义条件构造器
 *  </pre>
 *
 * @author xiuqun.hxq@alibaba-inc.com
 * @date 2019/3/18 下午10:42  03月 第 18天
 */
public class CustomFieldsConditionBuilder {

    private CustomFieldCondition customFieldCondition=new CustomFieldCondition();


    /**
     * 设置字段名称
     * @param fieldName
     * @return
     */
    public CustomFieldsConditionBuilder fieldName(String fieldName){
            customFieldCondition.setFieldName(fieldName);
            return this;
    }

    public CustomFieldsConditionBuilder fieldValue(String fieldValue){
        customFieldCondition.setFieldValue(fieldValue);
        return this;
    }

    public CustomFieldsConditionBuilder operator(String operator){
        customFieldCondition.setOperator(operator);
        return this;
    }

    public CustomFieldsConditionBuilder condition(String condition){
        customFieldCondition.setCondition(condition);
        return this;
    }


    /**
     * 返回一个自定义字段
     * @return
     */
    public CustomFieldCondition build() {
        return customFieldCondition;
    }
}

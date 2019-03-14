package com.alibaba.smart.framework.engine.service.param.query.condition;

import lombok.Data;

/**
 * CustomFieldCondition
 * <pre>
 * TODO 功能描述
 *  </pre>
 *
 * @author xiuqun.hxq@alibaba-inc.com
 * @date 2019/3/13 下午6:58  03月 第 13天
 */
public class CustomFieldCondition  extends CustomField{


    /**
     * 表达式,如and,or
     */
    private String condition;


    /**
     * 判断该条件字段是否合法
     * @return
     */
    public boolean isValid(){
        if(!super.isValid()){
            return false;
        }
        return ConditionType.valid(condition);
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}

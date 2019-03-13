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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}

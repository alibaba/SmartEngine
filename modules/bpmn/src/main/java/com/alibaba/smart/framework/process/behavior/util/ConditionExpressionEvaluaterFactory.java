package com.alibaba.smart.framework.process.behavior.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author 高海军 帝奇 Apr 24, 2016 4:44:08 PM 类名称考虑
 */
public class ConditionExpressionEvaluaterFactory {

    public static ConditionExpressionEvaluater createConditionExpression(String type) {

        // TODO 可以cache,不用每次new
        if (StringUtils.isBlank(type) || Objects.equals("mvel", type)) {
            return new MvelConditionExpression();
        } else {
            throw new RuntimeException("No ConditionExpression implements for type :" + type);
        }
    }
}

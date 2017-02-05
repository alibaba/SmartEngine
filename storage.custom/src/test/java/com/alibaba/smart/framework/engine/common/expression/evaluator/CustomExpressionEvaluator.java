package com.alibaba.smart.framework.engine.common.expression.evaluator;

import java.util.Map;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  22:17.
 */
public class CustomExpressionEvaluator implements  ExpressionEvaluator {
    @Override
    public boolean eval(String expression, Map<String, Object> vars) {
        return true;
    }
}

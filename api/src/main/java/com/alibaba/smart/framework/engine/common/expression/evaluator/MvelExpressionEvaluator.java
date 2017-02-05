package com.alibaba.smart.framework.engine.common.expression.evaluator;

import org.mvel2.MVEL;

import java.util.Map;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  15:51.
 */
public class MvelExpressionEvaluator implements ExpressionEvaluator {
    @Override
    public boolean eval(String expression, Map<String, Object> vars) {

        //TODO SUPPORT OTHER ,TODO  预编译

        Boolean result = MVEL.evalToBoolean(expression, vars);
        return   result.booleanValue();
    }
}

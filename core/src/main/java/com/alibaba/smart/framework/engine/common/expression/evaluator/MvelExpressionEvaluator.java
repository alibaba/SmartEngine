package com.alibaba.smart.framework.engine.common.expression.evaluator;

import com.alibaba.smart.framework.engine.common.util.MvelUtil;

import java.util.Map;

/** Created by 高海军 帝奇 74394 on 2017 February 15:51. */
public class MvelExpressionEvaluator implements ExpressionEvaluator {

    @Override
    public Object eval(String expression, Map<String, Object> vars, boolean needCached) {
        return MvelUtil.eval(expression, vars, needCached);
    }
}

package com.alibaba.smart.framework.engine.common.expression.evaluator;

import org.mvel2.MVEL;
import org.mvel2.ast.ASTNode;
import org.mvel2.ast.BinaryOperation;
import org.mvel2.compiler.ExecutableAccessor;

import java.io.Serializable;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.MvelUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  15:51.
 */
public class MvelExpressionEvaluator implements ExpressionEvaluator {

    @Override
    public Object eval(String expression, Map<String, Object> vars) {
        return MvelUtil.eval(expression,vars);
    }
}

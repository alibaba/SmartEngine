package com.alibaba.smart.framework.engine.common.expression;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.expression.evaluator.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ExpressionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionUtil.class);


    private static final String PACKAGE_NAME = "com.alibaba.smart.framework.engine.common.expression.evaluator.";
    private static final String EXPRESSION_EVALUATOR = "ExpressionEvaluator";

    public static Boolean eval(ExecutionContext context, ConditionExpression conditionExpression) {
        String type = conditionExpression.getExpressionType();

        if (null != type) {
            int expressionTypeSplitIndex = type.indexOf(":");
            if (expressionTypeSplitIndex >= 0) {
                type = type.substring(expressionTypeSplitIndex + 1);
            }
        } else {
            type = "mvel";
        }

        Object eval = ExpressionUtil.eval(type,
            conditionExpression.getExpressionContent(), context);

        return (Boolean)eval;
    }


    private static Object eval(String type, String expression, ExecutionContext context) {
        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();

        String firstCharToUpperCase = Character.toUpperCase(type.charAt(0)) + type.substring(1);

        String className = PACKAGE_NAME + firstCharToUpperCase + EXPRESSION_EVALUATOR;
        InstanceAccessor instanceAccessor = processEngineConfiguration
            .getInstanceAccessor();
        ExpressionEvaluator expressionEvaluator = (ExpressionEvaluator)instanceAccessor.access(className);

        if (null != expressionEvaluator) {
            Map<String,Object> requestContext=context.getRequest();

            Object result = expressionEvaluator.eval(expression, requestContext, processEngineConfiguration
                .isExpressionCompileResultCached());

            LOGGER.info("expressionEvaluator.result result is {}, each param is {} {} ",result,expression,requestContext);

            return result;
        } else {
            return null;
        }
    }
}

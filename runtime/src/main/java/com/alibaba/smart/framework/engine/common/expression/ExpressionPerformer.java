package com.alibaba.smart.framework.engine.common.expression;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.expression.evaluator.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.context.ExecutionContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public abstract class ExpressionPerformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionPerformer.class);


    private static final String PACKAGE_NAME = "com.alibaba.smart.framework.engine.common.expression.evaluator.";
    private static final String EXPRESSION_EVALUATOR = "ExpressionEvaluator";

    public static Object eval(String type, String expression, ExecutionContext context) {
        String firstCharToUpperCase = Character.toUpperCase(type.charAt(0)) + type.substring(1);

        String className = PACKAGE_NAME + firstCharToUpperCase + EXPRESSION_EVALUATOR;
        InstanceAccessor instanceAccessor = context.getProcessEngineConfiguration()
            .getInstanceAccessor();
        ExpressionEvaluator expressionEvaluator = (ExpressionEvaluator)instanceAccessor.access(className);

        if (null != expressionEvaluator) {
            Map<String,Object> requestContext=context.getRequest();

            Object result = expressionEvaluator.eval(expression, requestContext);

            LOGGER.info("expressionEvaluator.result result is {}, each param is {} {} ",result,expression,requestContext);

            return result;
        } else {
            return null;
        }
    }
}

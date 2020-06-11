package com.alibaba.smart.framework.engine.common.expression;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.expression.evaluator.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.AdHocConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.util.ClassUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExpressionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionUtil.class);


    private static final String PACKAGE_NAME = "com.alibaba.smart.framework.engine.common.expression.evaluator.";
    private static final String EXPRESSION_EVALUATOR = "ExpressionEvaluator";

    public static Boolean eval(ExecutionContext context, ConditionExpression conditionExpression) {
        String type = conditionExpression.getExpressionType();

        Object eval = ExpressionUtil.eval( context.getRequest(),type,
            conditionExpression.getExpressionContent(),context.getProcessEngineConfiguration());

        return (Boolean)eval;
    }

    public static Boolean eval( Map<String,Object> requestContext, ConditionExpression conditionExpression, ProcessEngineConfiguration processEngineConfiguration) {
        String type = conditionExpression.getExpressionType();
        String expressionContent = conditionExpression.getExpressionContent();

        Object eval = ExpressionUtil.eval(requestContext,type,
            expressionContent, processEngineConfiguration);

        return (Boolean)eval;
    }



    private static Object eval( Map<String,Object> requestContext,String type, String expression,  ProcessEngineConfiguration processEngineConfiguration) {

        // 兼容
        if (null != type) {
            int expressionTypeSplitIndex = type.indexOf(":");
            if (expressionTypeSplitIndex >= 0) {
                type = type.substring(expressionTypeSplitIndex + 1);
            }
        } else {
            type = AdHocConstant.MVEL;
        }

        String firstCharToUpperCase = Character.toUpperCase(type.charAt(0)) + type.substring(1);

        String className = PACKAGE_NAME + firstCharToUpperCase + EXPRESSION_EVALUATOR;

        ExpressionEvaluator expressionEvaluator = (ExpressionEvaluator)ClassUtil.createOrGetInstance(className);

        if (null != expressionEvaluator) {

            ConfigurationOption configurationOption = processEngineConfiguration
                .getOptionContainer().get(ConfigurationOption.EXPRESSION_COMPILE_RESULT_CACHED_OPTION.getId());

            Object result = expressionEvaluator.eval(expression, requestContext, configurationOption.isEnabled());

            LOGGER.info("expressionEvaluator.result result is {}, each param is {} {} ",result,expression,requestContext);

            return result;
        } else {

            throw  new EngineException("No Expression Evaluator found for "+ className);
        }
    }
}

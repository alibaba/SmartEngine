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

        ConfigurationOption configurationOption = processEngineConfiguration
            .getOptionContainer().get(ConfigurationOption.EXPRESSION_COMPILE_RESULT_CACHED_OPTION.getId());

        ExpressionEvaluator expressionEvaluator = processEngineConfiguration.getExpressionEvaluator();
        Object result = expressionEvaluator.eval(expression, requestContext, configurationOption.isEnabled());

        LOGGER.info("expressionEvaluator.result result is {}, each param is {} {} ",result,expression,requestContext);

        return result;

    }
}

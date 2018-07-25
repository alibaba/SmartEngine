package com.alibaba.smart.framework.engine.common.expression;

import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.expression.evaluator.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public abstract class ExpressionPerformer {
    private static final String PACKAGE_NAME = "com.alibaba.smart.framework.engine.common.expression.evaluator.";
    private static final String EXPRESSION_EVALUATOR = "ExpressionEvaluator";

    public static Object eval(ExtensionPointRegistry extensionPointRegistry, String type, String expression, ExecutionContext context) {
        String firstCharToUpperCase = Character.toUpperCase(type.charAt(0)) + type.substring(1);

        String className = PACKAGE_NAME + firstCharToUpperCase + EXPRESSION_EVALUATOR;
        InstanceAccessor instanceAccessor = extensionPointRegistry.getExtensionPoint(SmartEngine.class)
            .getProcessEngineConfiguration()
            .getInstanceAccessor();
        ExpressionEvaluator expressionEvaluator = (ExpressionEvaluator)instanceAccessor.access(className);

        if (null != expressionEvaluator) {
            Map<String,Object> privateContext=context.getPrivateContext();
            Map<String,Object> requestContext=context.getRequest();
            if(null!=privateContext && null!=requestContext){
                //TODO ettear 不够优雅，后续结合Scope再一起优化
                privateContext.putAll(requestContext);
            }
            return expressionEvaluator.eval(expression, privateContext);
        } else {
            return null;
        }
    }
}

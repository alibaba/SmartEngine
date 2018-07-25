package com.alibaba.smart.framework.engine.modules.bpmn.provider.expression;

import com.alibaba.smart.framework.engine.common.expression.ExpressionPerformer;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.provider.Performer;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class ConditionExpressionProvider implements Performer {
    private ExtensionPointRegistry extensionPointRegistry;
    private ConditionExpression conditionExpression;

    ConditionExpressionProvider(ExtensionPointRegistry extensionPointRegistry, ConditionExpression conditionExpression) {
        this.extensionPointRegistry=extensionPointRegistry;
        this.conditionExpression = conditionExpression;
    }

    @Override
    public Object perform(ExecutionContext context) {
        if (null != this.conditionExpression) {
            String type = this.conditionExpression.getExpressionType();

            if (null != type) {
                int expressionTypeSplitIndex = type.indexOf(":");
                if (expressionTypeSplitIndex >= 0) {
                    type = type.substring(expressionTypeSplitIndex + 1);
                }
            } else {
                type = "mvel";
            }

            return ExpressionPerformer.eval(this.extensionPointRegistry,type,
                this.conditionExpression.getExpressionContent(), context);
        }
        return null;
    }
}

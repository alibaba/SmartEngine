package com.alibaba.smart.framework.engine.modules.bpmn.provider.expression;

import com.alibaba.smart.framework.engine.common.expression.ExpressionPerformer;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.provider.Performer;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class ConditionExpressionProvider implements Performer {
    private ConditionExpression conditionExpression;

    ConditionExpressionProvider(ConditionExpression conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    @Override
    public Object perform(ExecutionContext context) {
        if (null != this.conditionExpression) {
            String type = this.conditionExpression.getExpressionType();

            if (null != type) {
                if("condition".equals(type)){
                    return this.conditionExpression.getCondition().check(context);
                }
                int expressionTypeSplitIndex = type.indexOf(":");
                if (expressionTypeSplitIndex >= 0) {
                    type = type.substring(expressionTypeSplitIndex + 1);
                }
            } else {
                type = "mvel";
            }

            return ExpressionPerformer.eval(type,
                this.conditionExpression.getExpressionContent(), context);
        }
        return null;
    }
}

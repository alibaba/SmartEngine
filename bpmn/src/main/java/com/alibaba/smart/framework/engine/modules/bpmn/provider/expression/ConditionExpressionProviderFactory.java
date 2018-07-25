package com.alibaba.smart.framework.engine.modules.bpmn.provider.expression;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmElement;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class ConditionExpressionProviderFactory implements PerformerProviderFactory<ConditionExpression> {


    private ExtensionPointRegistry extensionPointRegistry;

    public ConditionExpressionProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public Performer createPerformer(PvmElement pvmElement, ConditionExpression conditionExpression) {
        return new ConditionExpressionProvider(this.extensionPointRegistry,conditionExpression);
    }

    @Override
    public Class<ConditionExpression> getModelType() {
        return ConditionExpression.class;
    }
}

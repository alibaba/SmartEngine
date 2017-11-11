package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopCardinality;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class LoopCardinalityProvider implements LoopCollectionProvider {
    private ExtensionPointRegistry extensionPointRegistry;
    private Performer cardinalityExpressionPerformer;

    LoopCardinalityProvider(ExtensionPointRegistry extensionPointRegistry, LoopCardinality loopCardinality) {
        this.extensionPointRegistry = extensionPointRegistry;
        ProviderFactoryExtensionPoint providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
            ProviderFactoryExtensionPoint.class);

        Performable cardinalityExpression = loopCardinality.getCardinalityExpression();
        if (null != cardinalityExpression) {

            PerformerProviderFactory cardinalityExpressionProviderFactory
                = (PerformerProviderFactory)providerFactoryExtensionPoint
                .getProviderFactory(cardinalityExpression.getClass());
            this.cardinalityExpressionPerformer = cardinalityExpressionProviderFactory.createPerformer(null,
                cardinalityExpression);
        }
    }

    @Override
    public Collection<Object> getCollection(ExecutionContext context, PvmActivity activity) {
        if (null != this.cardinalityExpressionPerformer) {
            Integer integer = (Integer)this.cardinalityExpressionPerformer.perform(context);
            if (null != integer && integer > 0) {
                List<Object> collection = new ArrayList<Object>(integer);
                for (int i = 0; i < integer; i++) {
                    collection.add(i);
                }
                return collection;
            }
        }
        return null;
    }
}

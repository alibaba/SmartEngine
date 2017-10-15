package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionChecker;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class CompletionCheckerProvider {
    private Performer completionCheckPerformer;
    private Performer abortCheckPerformer;

    CompletionCheckerProvider(ExtensionPointRegistry extensionPointRegistry, CompletionChecker completionChecker) {
        ProviderFactoryExtensionPoint providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
            ProviderFactoryExtensionPoint.class);

        Performable completionPerformable = completionChecker.getCompletionCheckPerformable();
        if (null != completionPerformable) {

            PerformerProviderFactory completionPerformerProviderFactory
                = (PerformerProviderFactory)providerFactoryExtensionPoint
                .getProviderFactory(completionPerformable.getClass());
            this.completionCheckPerformer = completionPerformerProviderFactory.createPerformer(completionPerformable);
        }

        Performable abortPerformable = completionChecker.getAbortCheckPerformable();
        if (null != abortPerformable) {

            PerformerProviderFactory abortPerformerProviderFactory
                = (PerformerProviderFactory)providerFactoryExtensionPoint
                .getProviderFactory(abortPerformable.getClass());
            this.abortCheckPerformer = abortPerformerProviderFactory.createPerformer(abortPerformable);
        }
    }

    public Performer getCompletionCheckPerformer() {
        return completionCheckPerformer;
    }

    public Performer getAbortCheckPerformer() {
        return abortCheckPerformer;
    }
}

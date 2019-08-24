package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionChecker;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class CompletionCheckerProvider {
    protected Performer completionCheckPerformer;
    protected Performer abortCheckPerformer;

    CompletionCheckerProvider(ExtensionPointRegistry extensionPointRegistry, CompletionChecker completionChecker) {
        ProviderFactoryExtensionPoint providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
            ProviderFactoryExtensionPoint.class);

        //Performable completionPerformable = completionChecker.getCompletionCheckPerformable();
        //if (null != completionPerformable) {
        //
        //    PerformerProviderFactory completionPerformerProviderFactory
        //        = (PerformerProviderFactory)providerFactoryExtensionPoint
        //        .getProviderFactory(completionPerformable.getClass());
        //    this.completionCheckPerformer = completionPerformerProviderFactory.createPerformer(null, completionPerformable);
        //}
        //
        //Performable abortPerformable = completionChecker.getAbortCheckPerformable();
        //if (null != abortPerformable) {
        //
        //    PerformerProviderFactory abortPerformerProviderFactory
        //        = (PerformerProviderFactory)providerFactoryExtensionPoint
        //        .getProviderFactory(abortPerformable.getClass());
        //    this.abortCheckPerformer = abortPerformerProviderFactory.createPerformer(null, abortPerformable);
        //}
    }

    public Performer getCompletionCheckPerformer() {
        return completionCheckPerformer;
    }

    public Performer getAbortCheckPerformer() {
        return abortCheckPerformer;
    }
}

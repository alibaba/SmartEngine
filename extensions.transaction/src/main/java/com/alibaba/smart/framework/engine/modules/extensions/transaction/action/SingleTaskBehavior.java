package com.alibaba.smart.framework.engine.modules.extensions.transaction.action;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author Leo.yy   Created on 2017/8/2.
 * @description
 * @see
 */
public class SingleTaskBehavior  extends AbstractActivityBehavior<SingleTask> implements ActivityBehavior<SingleTask> {

    public SingleTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity pvmActivity) {
        super(extensionPointRegistry, pvmActivity);
    }

    @Override
    protected void buildInstanceRelationShip(PvmActivity runtimeActivity, ExecutionContext context) {

    }
}

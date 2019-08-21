package com.alibaba.smart.framework.engine.modules.smart.provider.process;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartTask;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author ettear
 * Created by ettear on 07/08/2017.
 */
public class TaskBehavior extends AbstractActivityBehavior<SmartTask> {
    public TaskBehavior(
        ExtensionPointRegistry extensionPointRegistry,
        PvmActivity pvmActivity) {
        super(extensionPointRegistry, pvmActivity);
    }
}

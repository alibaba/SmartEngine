package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.IntermediateCatchEvent;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 上午11:11
 */
public class IntermediateCatchEventBehavior extends AbstractActivityBehavior<IntermediateCatchEvent> {

    public IntermediateCatchEventBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    //TODO
}

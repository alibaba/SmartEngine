package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import java.util.Collection;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public interface LoopCollectionProvider {
    Collection<Object> getCollection(ExecutionContext context,
                                     PvmActivity activity);
}

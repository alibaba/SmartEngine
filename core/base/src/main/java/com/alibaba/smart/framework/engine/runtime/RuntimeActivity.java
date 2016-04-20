package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.context.InstanceContext;

import java.util.Map;

/**
 * Created by ettear on 16-4-11.
 */
public interface RuntimeActivity<M extends Activity> extends RuntimeInvocable<M> {

    Map<String, RuntimeSequenceFlow> getIncomeSequenceFlows();

    Map<String, RuntimeSequenceFlow> getOutcomeSequenceFlows();

    boolean execute(InstanceContext context);

    boolean isStartActivity();
}

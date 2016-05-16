package com.alibaba.smart.framework.engine.runtime;

import java.util.Map;

import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * RuntimeActivity
 * Created by ettear on 16-4-11.
 */
public interface RuntimeActivity extends RuntimeInvocable<Activity> {
    Map<String, RuntimeTransition> getIncomeTransitions();

    Map<String, RuntimeTransition> getOutcomeTransitions();

    Message execute(InstanceContext context);

    boolean isStartActivity();
}

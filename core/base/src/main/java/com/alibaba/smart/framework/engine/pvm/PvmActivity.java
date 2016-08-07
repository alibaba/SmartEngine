package com.alibaba.smart.framework.engine.pvm;

import java.util.Map;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.model.assembly.Activity;

/**
 * RuntimeActivity Created by ettear on 16-4-11.
 */
public interface PvmActivity extends PvmInvocable<Activity> {

    Map<String, PvmTransition> getIncomeTransitions();

    Map<String, PvmTransition> getOutcomeTransitions();

    Message execute(InstanceContext context);

    boolean isStartActivity();
}
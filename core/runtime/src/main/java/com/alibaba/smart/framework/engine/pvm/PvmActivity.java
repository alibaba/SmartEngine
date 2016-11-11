package com.alibaba.smart.framework.engine.pvm;

import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ProviderRegister;

/**
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
public interface PvmActivity extends PvmInvocable<Activity>,ProviderRegister {

    Map<String, PvmTransition> getIncomeTransitions();

    Map<String, PvmTransition> getOutcomeTransitions();

    /**
     * 流程实例启动,节点进入 会调用此方法.
     * @param context
     */
    Message execute(ExecutionContext context);
    
    /**
     * 暂停型节点恢复执行时,会调用此方法.
     * @param context
     */
    Message signal(ExecutionContext context);


}

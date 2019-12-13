package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPvmActivity extends AbstractPvmElement<Activity> implements PvmActivity {

    private Map<String, PvmTransition> incomeTransitions = new ConcurrentHashMap<String, PvmTransition>();
    private Map<String, PvmTransition> outcomeTransitions = new ConcurrentHashMap<String, PvmTransition>();

    private ActivityBehavior behavior;

    protected AbstractPvmActivity(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }


    public void addIncomeTransition(String transitionId, PvmTransition income) {
        this.incomeTransitions.put(transitionId, income);
    }

    public void addOutcomeTransition(String transitionId, PvmTransition outcome) {
        this.outcomeTransitions.put(transitionId, outcome);
    }
}

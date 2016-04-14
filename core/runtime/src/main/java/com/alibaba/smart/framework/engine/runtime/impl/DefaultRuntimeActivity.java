package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultRuntimeActivity
 * Created by ettear on 16-4-13.
 */
@Data
public class DefaultRuntimeActivity<M extends Activity> extends AbstractRuntimeInvocable<M>
        implements RuntimeActivity<M> {

    private Map<String, RuntimeSequenceFlow> incomeSequenceFlows  = new ConcurrentHashMap<>();
    private Map<String, RuntimeSequenceFlow> outcomeSequenceFlows = new ConcurrentHashMap<>();

    public void addIncomeSequenceFlows(String sequenceId, RuntimeSequenceFlow income) {
        incomeSequenceFlows.put(sequenceId, income);
    }

    public void addOutcomeSequenceFlows(String sequenceId, RuntimeSequenceFlow outcome) {
        outcomeSequenceFlows.put(sequenceId, outcome);
    }
}

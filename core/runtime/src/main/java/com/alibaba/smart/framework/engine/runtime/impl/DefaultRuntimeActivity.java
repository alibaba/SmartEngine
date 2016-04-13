package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.engine.context.Context;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public class DefaultRuntimeActivity extends AbstractActivity implements RuntimeActivity {

    private Class<?> modelType;
    private Invoker  invoker;
    private Map<String, RuntimeSequenceFlow> incomeSequenceFlows  = new ConcurrentHashMap<>();
    private Map<String, RuntimeSequenceFlow> outcomeSequenceFlows = new ConcurrentHashMap<>();

    @Override
    public Message invoke(Context context) {
        return invoker.invoke(context);
    }

    @Override
    public Message invokeAsync(Context context) {
        return invoker.invoke(context);
    }

    public void addIncomeSequenceFlows(String sequenceId, RuntimeSequenceFlow income) {
        incomeSequenceFlows.put(sequenceId, income);
    }

    public void addOutcomeSequenceFlows(String sequenceId, RuntimeSequenceFlow outcome) {
        outcomeSequenceFlows.put(sequenceId, outcome);
    }

}

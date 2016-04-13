package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.Activity;

import java.util.Map;

/**
 * Created by ettear on 16-4-11.
 */
public interface RuntimeActivity extends Activity, Invocable {

    Class<?> getModelType();

    Map<String, RuntimeSequenceFlow> getIncomeSequenceFlows();

    Map<String, RuntimeSequenceFlow> getOutcomeSequenceFlows();
}

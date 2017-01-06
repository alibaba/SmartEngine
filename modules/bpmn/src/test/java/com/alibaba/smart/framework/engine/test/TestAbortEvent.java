package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.invocation.signal.AbortSignal;
import com.alibaba.smart.framework.engine.invocation.signal.Signal;
import com.alibaba.smart.framework.engine.modules.bpmn.TestRunTimeException;

import java.util.Map;

/**
 * @author dongdong.zdd
 * @since 2016-12-13
 */
public class TestAbortEvent {

    public void process(Map<String,Object> context) {
       throw new TestRunTimeException("test txcetprion");
    }

}

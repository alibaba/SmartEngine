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

    private int entrance = 0;

    public void process(Map<String,Object> context) {
        if (entrance == 1) {
            System.out.println("context = [" + context + "]");
        }
        if (entrance == 0) {
            entrance++;
            throw new TestRunTimeException("test txcetprion");
        }

    }

}

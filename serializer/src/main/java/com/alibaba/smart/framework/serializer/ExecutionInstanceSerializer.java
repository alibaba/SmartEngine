package com.alibaba.smart.framework.serializer;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.param.ExecutionParam;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  20:23.
 */
public class ExecutionInstanceSerializer {
    public static String toString(ExecutionInstance executionInstance){
        return nullToEmpty(executionInstance.getInstanceId().toString())
                + "|"
                + nullToEmpty(executionInstance.getActivityId().toString())
                + "|";
    }

//    @Override
//    public ExecutionInstance getModel(ExecutionParam param) {
//        ExecutionInstance executionInstance = new DefExecutionInstance executionInstance
//        this.setProcessInstanceId(param.getProcessId());
//        this.setInstanceId(param.getExecutionId());
//        return this;
//
//    }

    private static String nullToEmpty( String string) {
        return (string == null) ? "" : string;
    }
}

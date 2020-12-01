package com.alibaba.smart.framework.engine.test.parallelgateway;

public class ServiceTaskTimeoutOrchestrationDelegationTwo extends ServiceTaskTimeoutOrchestrationDelegation {

    @Override
    protected int waitTime() {
        return 500;
    }

    @Override
    protected String taskId() {
        return "taskB";
    }
}

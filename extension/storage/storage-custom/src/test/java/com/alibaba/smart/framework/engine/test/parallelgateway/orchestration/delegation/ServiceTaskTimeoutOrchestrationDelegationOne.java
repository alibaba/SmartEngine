package com.alibaba.smart.framework.engine.test.parallelgateway.orchestration.delegation;

public class ServiceTaskTimeoutOrchestrationDelegationOne extends ServiceTaskTimeoutOrchestrationDelegation {

    @Override
    protected int waitTime() {
        return 1000;
    }

    @Override
    protected String taskId() {
        return "taskA";
    }
}

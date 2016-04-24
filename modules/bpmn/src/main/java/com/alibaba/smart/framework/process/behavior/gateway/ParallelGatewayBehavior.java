package com.alibaba.smart.framework.process.behavior.gateway;

import com.alibaba.smart.framework.process.behavior.AbstractActivityBehavior;


public class ParallelGatewayBehavior  extends AbstractActivityBehavior {
    @Override
    public void execute() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void signal() {
        //TODO add custom exception
        throw new RuntimeException("this activity doesn't accept signals");        
    }
}

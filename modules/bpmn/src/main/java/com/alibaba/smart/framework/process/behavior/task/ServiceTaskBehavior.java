package com.alibaba.smart.framework.process.behavior.task;

import com.alibaba.smart.framework.process.behavior.AbstractActivityBehavior;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
public class ServiceTaskBehavior extends AbstractActivityBehavior {

    @Override
    public void execute() {


    }

    @Override
    public void signal() {
        // TODO add custom exception
        throw new RuntimeException("this activity doesn't accept signals");
    }

}

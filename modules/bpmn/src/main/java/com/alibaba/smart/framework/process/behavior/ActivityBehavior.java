package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.process.context.ProcessContext;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:13 PM
 */
public interface ActivityBehavior {

    public void execute(ProcessContext context);
}

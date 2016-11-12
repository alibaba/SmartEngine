package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.Process;
import com.alibaba.smart.framework.engine.provider.ActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author 高海军 帝奇 Apr 21, 2016 8:28:49 PM TODO 删除该类? 每个xml element 扩展点需要定义3个扩展点,略繁琐
 */
public class ProcessBehaviorProvider extends AbstractActivityBehaviorProvider<Process> implements ActivityBehaviorProvider<Process> {

    public ProcessBehaviorProvider(PvmActivity runtimeActivity) {
        super(runtimeActivity);
    }

    @Override
    public void execute(PvmActivity runtimeActivity, ExecutionContext context) {

    }
}

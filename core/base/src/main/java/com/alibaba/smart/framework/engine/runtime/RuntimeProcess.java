package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.context.InstanceContext;

import java.util.Map;

/**
 * RuntimeProcess
 * Created by ettear on 16-4-12.
 */
public interface RuntimeProcess extends RuntimeActivity<Process> {

    Map<String, RuntimeActivity> getActivities();

    /**
     * 运行流程
     * @param context 实例上下文
     * @return 是否暂停
     */
    boolean run(InstanceContext context);

    /**
     * 继续执行流程
     * @param context 实例上下文
     * @return 是否暂停
     */
    boolean resume(InstanceContext context);

}

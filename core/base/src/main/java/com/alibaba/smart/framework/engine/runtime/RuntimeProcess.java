package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * RuntimeProcess Created by ettear on 16-4-12.
 */
public interface RuntimeProcess extends RuntimeActivity {

    String getUri();

    void setUri(String uri);

    RuntimeActivity getStartActivity();

    @Override
    Process getModel();

    /**
     * 运行流程
     * 
     * @param context 实例上下文
     * @return 是否暂停
     */
    Message run(InstanceContext context);

    /**
     * 继续执行流程
     * 
     * @param context 实例上下文
     * @return 是否暂停
     */
    Message resume(InstanceContext context);

}

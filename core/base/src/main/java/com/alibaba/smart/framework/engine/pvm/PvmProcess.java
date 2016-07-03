package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.model.artifact.Process;

/**
 * RuntimeProcess Created by ettear on 16-4-12.
 */
public interface PvmProcess extends PvmActivity {

    String getUri();

    void setUri(String uri);

    PvmActivity getStartActivity();

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

package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.model.assembly.Process;

import java.util.Map;

/**
 * RuntimeProcess Created by ettear on 16-4-12.
 */
public interface PvmProcessDefinition extends PvmActivity {

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
    Message run(ExecutionContext context);

    /**
     * 继续执行流程
     * 
     * @param context 实例上下文
     * @return 是否暂停
     */
    Message resume(ExecutionContext context);


    Map<String,PvmActivity> getActivities();

}

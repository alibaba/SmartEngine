package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.model.assembly.Process;

import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmProcessDefinition {

    String getUri();

    void setUri(String uri);

    PvmActivity getStartActivity();

    Process getModel();

//    /**
//     * 运行流程
//     *
//     * @param context 实例上下文
//     * @return 是否暂停
//     */
//    void run(ExecutionContext context);
//
//    /**
//     * 继续执行流程
//     *
//     * @param context 实例上下文
//     * @return 是否暂停
//     */
//    void resume(ExecutionContext context);


    Map<String,PvmActivity> getActivities();

}

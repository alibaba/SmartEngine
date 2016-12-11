package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */

public interface ProcessCommandService {


    ProcessInstance start(String processId, String version, Map<String, Object> variables);

    ProcessInstance start(String processId, String version);


    void abort(Long processInstanceId);

    void abort(Long processInstanceId,String reason);

//    /**
//     * 查找某个流程
//     *
//     * @param processInstanceId
//     * @return
//     */
//    ProcessInstance find(String processInstanceId);

//    /**
//     * 从参数中恢复流程实例到内存中
//     *
//     * @param processParam
//     * @return
//     */
//    void recovery(EngineParam processParam);


//    /**
//     * 指定activityId来运行流程
//     *
//     * @param processId
//     * @param activityId
//     * @return
//     */
//    ProcessInstance run(ProcessDefinition definition,String processId, String activityId, boolean sub,Map<String,Object> request);


//    void clear(String processId);


}

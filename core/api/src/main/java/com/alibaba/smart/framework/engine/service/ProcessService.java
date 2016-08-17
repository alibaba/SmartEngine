package com.alibaba.smart.framework.engine.service;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.EngineParam;
import com.alibaba.smart.framework.engine.result.EngineResult;


public interface ProcessService {

    /**
     * 启动流程
     * @param processId
     * @param version
     * @param variables
     * @return
     */
    ProcessInstance start(String processId, String version, Map<String, Object> variables);

    /**
     * 终止
     * @param processInstanceId
     */
    void abort(String processInstanceId);

    /**
     * 查找某个流程
     * @param processInstanceId
     * @return
     */
    ProcessInstance find(String processInstanceId);

    /**
     * 从参数中恢复流程实例到内存中
     * @param processParam
     * @return
     */
    void recovery(EngineParam processParam);


    /**
     * 指定activityId来运行流程
     *
     * @param processId
     * @param activityId
     * @return
     */
    ProcessInstance run(ProcessDefinition definition,String processId, String activityId, boolean sub);

    void clear(String processId);


    EngineResult toDatabase(String processId);



}

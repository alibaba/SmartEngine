package com.alibaba.smart.framework.engine.service;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.EngineParam;

import java.util.Map;


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
    ProcessInstance recovery(EngineParam processParam);


    /**
     * 指定activityId来运行流程
     *
     * @param processId
     * @param activityId
     * @return
     */
    ProcessInstance run(ProcessDefinition definition,String processId, String activityId, boolean sub,Map<String,Object> request);


    ProcessInstance run(ProcessDefinition definition,ProcessInstance processInstance, String activityId,Map<String,Object> request);


    void clear(String processId);


    /**
     * 在内存中推进流程引擎
     * @return
     */
    ProcessInstance pushActivityOnRam(ProcessDefinition definition,String processId);




}

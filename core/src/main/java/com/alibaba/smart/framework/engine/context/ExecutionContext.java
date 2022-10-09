package com.alibaba.smart.framework.engine.context;

import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;


public interface ExecutionContext {


    /**
     * 前提: 目前SE中仅 fork 后的链路会存在并发执行的场景; 当并发执行时,如下这几个变量会被并发修改. 所以需要在fork时,创建独立的context对象,避免导致数据紊乱.
     *
     *  START 1:
     */

    ExecutionInstance getExecutionInstance();

    void setExecutionInstance(ExecutionInstance executionInstance);

    BaseElement getBaseElement();

    void setBaseElement(BaseElement baseElement);

    ActivityInstance getActivityInstance();

    void setActivityInstance(ActivityInstance activityInstance);

    /* END 1 */


    /**
     * 前提: 如果需要在并发场景使用,则一般应优先考虑使用ConcurrentHashMap来代替.
     *
     *  START 2:
     */

    Map<String, Object> getRequest();

    void setRequest(Map<String, Object> request);

    void setResponse(Map<String, Object> response);

    Map<String, Object> getResponse();

    /* END 2 */





    /**
     * 前提: 理论上,执行期间应该是只读对象 (ProcessDefinition 在reDeploy时,会发生改变).
     *
     *  START 3:
     */
    ProcessDefinition getProcessDefinition();

    void setProcessDefinition(ProcessDefinition processDefinition);

    ProcessEngineConfiguration getProcessEngineConfiguration();

    void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration);

    /* END 3 */



    ExecutionContext getParent();

    void setParent(ExecutionContext parent);

    ProcessInstance getProcessInstance();

    void setProcessInstance(ProcessInstance processInstance);


    void setNeedPause(boolean needPause);

    boolean isNeedPause();


    Long getBlockId();

    void setBlockId(Long blockId);



}

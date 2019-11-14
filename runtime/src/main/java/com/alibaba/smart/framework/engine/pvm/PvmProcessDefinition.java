package com.alibaba.smart.framework.engine.pvm;


import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmProcessDefinition {

    String getId();

    String getVersion();

    String getUri();

    void setUri(String uri);

    PvmActivity getStartActivity();

    ProcessDefinition getModel();

    Map<String, PvmActivity> getActivities();

}

package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.model.assembly.Process;

import java.util.Map;

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

    Process getModel();

    Map<String, PvmActivity> getActivities();

}

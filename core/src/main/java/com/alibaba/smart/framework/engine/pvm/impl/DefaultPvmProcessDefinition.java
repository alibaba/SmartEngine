package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import lombok.Data;

import java.util.Map;

/**
 * @author 高海军 帝奇 2016.11.11
 * @author ettear 2016.04.13
 */
@Data
public class DefaultPvmProcessDefinition implements PvmProcessDefinition {

    private String idAndVersion;

    private String id;

    private String version;

    private String tenantId;

    private Map<String, PvmActivity> activities;

    private Map<String, PvmTransition> transitions;

    private PvmActivity startActivity;

    private ProcessDefinition model;
}

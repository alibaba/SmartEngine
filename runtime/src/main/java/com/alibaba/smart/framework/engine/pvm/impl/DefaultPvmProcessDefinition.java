package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.Process;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import lombok.Data;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
public class DefaultPvmProcessDefinition implements PvmProcessDefinition {

    private String uri;

    private String id;

    private String version;

    private Map<String, PvmActivity> activities;

    private Map<String, PvmTransition> transitions;

    private PvmActivity startActivity;

    private Process model;


}

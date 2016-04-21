package com.alibaba.smart.framework.process.model.bpmn.assembly;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.process.model.bpmn.assembly.activity.ProcessActivity;

import lombok.Data;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@Data
public class ProcessDefinition {

    private Long                  id;
    private String                name;
    private Integer               version;

    private List<ProcessActivity> nodeList = new ArrayList<ProcessActivity>();
}

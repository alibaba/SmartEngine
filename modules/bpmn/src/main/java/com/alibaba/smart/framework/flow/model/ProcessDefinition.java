package com.alibaba.smart.framework.flow.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.alibaba.smart.framework.flow.model.bpmn.ProcessActivity;

@Data
public class ProcessDefinition  {

    private Long                  id;
    private String                name;
    private Integer               version;

    private List<ProcessActivity> nodeList = new ArrayList<ProcessActivity>();
}

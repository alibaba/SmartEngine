package com.alibaba.smart.framework.engine.bpmn.assembly.process;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionSource;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

@Data
public class ProcessDefinitionSourceImpl implements ProcessDefinitionSource {

    public static final QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "definitions");

    private static final long serialVersionUID = -7973338663278156625L;

    private List<ProcessDefinition> processDefinitionList = new ArrayList<ProcessDefinition>();

    private String tenantId;

    @Override
    public ProcessDefinition getFirstProcessDefinition() {
        return processDefinitionList.get(0);
    }
}

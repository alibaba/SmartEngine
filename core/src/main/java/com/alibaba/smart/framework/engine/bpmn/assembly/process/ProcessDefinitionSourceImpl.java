package com.alibaba.smart.framework.engine.bpmn.assembly.process;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionSource;

import lombok.Data;

@Data
public class ProcessDefinitionSourceImpl implements ProcessDefinitionSource {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "definitions");

    private static final long serialVersionUID = -7973338663278156625L;

    private List<ProcessDefinition> processDefinitionList = new ArrayList<ProcessDefinition>();

    @Override
    public ProcessDefinition getFirstProcessDefinition() {
        return processDefinitionList.get(0);
    }
}

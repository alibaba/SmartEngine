package com.alibaba.smart.framework.engine.ecology.designer.converter;

import com.alibaba.smart.framework.engine.ecology.designer.bpmn.BpmnModel;
import com.alibaba.smart.framework.engine.ecology.designer.element.bean.ProcessFlowModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * JSON到BPMN XML转换器
 */
public class JsonToBpmnConverter {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BpmnModelConverter modelConverter = new BpmnModelConverter();
    private final BpmnXmlGenerator xmlGenerator = new BpmnXmlGenerator();
    
    /**
     * 完整转换流程: JSON字符串 → ProcessFlowModel → BPMN对象 → XML字符串
     */
    public String convert(String jsonString) throws IOException {
        // 1. JSON String → ProcessFlowModel
        ProcessFlowModel flowModel = objectMapper.readValue(jsonString, ProcessFlowModel.class);
        
        // 2. ProcessFlowModel → BpmnModel
        BpmnModel bpmnModel = modelConverter.convert(flowModel);
        
        // 3. BpmnModel → XML String
        String xmlString = xmlGenerator.generate(bpmnModel);
        
        return xmlString;
    }
    
    /**
     * 从ProcessFlowModel对象转换
     */
    public String convert(ProcessFlowModel flowModel) {
        // 1. ProcessFlowModel → BpmnModel
        BpmnModel bpmnModel = modelConverter.convert(flowModel);
        
        // 2. BpmnModel → XML String
        String xmlString = xmlGenerator.generate(bpmnModel);
        
        return xmlString;
    }
}

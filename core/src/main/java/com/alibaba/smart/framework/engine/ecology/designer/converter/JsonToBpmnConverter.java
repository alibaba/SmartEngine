package com.alibaba.smart.framework.engine.ecology.designer.converter;

import com.alibaba.smart.framework.engine.ecology.designer.bpmn.BpmnModel;
import com.alibaba.smart.framework.engine.ecology.designer.element.bean.ProcessFlowModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * JSON到BPMN XML转换器
 */
public abstract class JsonToBpmnConverter {
    

    /**
     * 完整转换流程: JSON字符串 → ProcessFlowModel → BPMN对象 → XML字符串
     */
    public static String convert(String jsonString) throws IOException {

          final ObjectMapper objectMapper = new ObjectMapper();
          final BpmnModelConverter modelConverter = new BpmnModelConverter();
          final BpmnXmlGenerator xmlGenerator = new BpmnXmlGenerator();

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
    public static String convert(ProcessFlowModel flowModel) {
        final BpmnModelConverter modelConverter = new BpmnModelConverter();
        final BpmnXmlGenerator xmlGenerator = new BpmnXmlGenerator();

        // 1. ProcessFlowModel → BpmnModel
        BpmnModel bpmnModel = modelConverter.convert(flowModel);
        
        // 2. BpmnModel → XML String
        String xmlString = xmlGenerator.generate(bpmnModel);
        
        return xmlString;
    }
}

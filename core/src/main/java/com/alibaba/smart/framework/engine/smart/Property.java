package com.alibaba.smart.framework.engine.smart;

import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.constant.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import lombok.Data;


@Data
public class Property implements PropertiesElementMarker, NoneIdBasedElement {
    public final static List<QName> qtypes = Arrays.asList(
            new QName(SmartBase.SMART_NS, "property"),
            new QName(BpmnNameSpaceConstant.CAMUNDA_NAME_SPACE, "property", "camunda"),
            new QName(BpmnNameSpaceConstant.FLOWABLE_NAME_SPACE, "property", "flowable"),
            new QName(BpmnNameSpaceConstant.ACTIVITI_NAME_SPACE, "property", "activiti")
    );

    private String name;
    private String value;
    private String type;


    @Override
    public String getDecoratorType() {
        return ExtensionElementsConstant.PROPERTIES;
    }


}

package com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.ExtensionContainer;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by ettear on 16-4-29.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExtensionElements extends ExtensionContainer {

    /**
     *
     */
    private static final long serialVersionUID = -5080932640599337544L;
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "extensionElements");

}

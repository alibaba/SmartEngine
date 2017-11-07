package com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension;

import com.alibaba.smart.framework.engine.model.assembly.Extensions;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * Created by ettear on 16-4-29.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExtensionElements extends Extensions {

    /**
     *
     */
    private static final long serialVersionUID = -5080932640599337544L;
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "extensionElements");

}

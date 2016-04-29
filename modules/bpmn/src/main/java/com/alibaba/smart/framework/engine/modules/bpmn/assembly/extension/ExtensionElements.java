package com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;
import com.alibaba.smart.framework.engine.modules.common.assembly.Extensions;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * Created by ettear on 16-4-29.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExtensionElements extends Extensions {
    public final static QName type = new QName(BpmnBase.NAME_SPACE, "extensionElements");

}

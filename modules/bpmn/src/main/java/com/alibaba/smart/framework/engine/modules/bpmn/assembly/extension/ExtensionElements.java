package com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.model.assembly.Extensions;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;

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
	public final static QName type = new QName(BpmnBase.NAME_SPACE, "extensionElements");

}

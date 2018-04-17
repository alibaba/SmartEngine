package com.alibaba.smart.framework.engine.modules.smart.assembly.process;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTransition;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SequenceFlow extends AbstractTransition {

    public final static QName type = new QName(SmartBase.SMART_NS, "sequenceFlow");

}

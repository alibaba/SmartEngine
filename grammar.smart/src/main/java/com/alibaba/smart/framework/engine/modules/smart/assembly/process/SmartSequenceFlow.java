package com.alibaba.smart.framework.engine.modules.smart.assembly.process;

import java.util.List;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTransition;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmartSequenceFlow extends AbstractTransition {

    public final static QName type = new QName(SmartBase.SMART_NS, "sequenceFlow");

    @Override
    public ConditionExpression getConditionExpression() {
        return null;
    }
}

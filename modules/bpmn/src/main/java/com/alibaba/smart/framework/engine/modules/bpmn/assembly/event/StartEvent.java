package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 *         TODO 单独拿出一个来?  AbstractActivity 没有startActivity 这个属性? 有些sid编译告警.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartEvent extends AbstractEvent {

    //TODO 通过接口,而不是一个默认的属性来指定 type

    public final static QName type = new QName(BpmnBase.NAME_SPACE, "startEvent");

    private static final long serialVersionUID = 8769494440379002970L;

    @Override
    public boolean isStartActivity() {
        return true;
    }

}

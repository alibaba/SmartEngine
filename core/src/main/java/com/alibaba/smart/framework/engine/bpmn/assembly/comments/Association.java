package com.alibaba.smart.framework.engine.bpmn.assembly.comments;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;
import lombok.Data;

import javax.xml.namespace.QName;

/**
 * @author guoxing 2020年11月24日14:07:14
 */
@Data
public class Association implements NoneIdBasedElement {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE,
            "association");
    private static final long serialVersionUID = 6357539757300185621L;

}

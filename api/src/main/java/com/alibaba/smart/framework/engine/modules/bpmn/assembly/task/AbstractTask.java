package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractActivity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractTask extends AbstractActivity {

    /**
     *
     */
    private static final long serialVersionUID = 5042056118774610434L;

    private Map<String,String> properties;


}

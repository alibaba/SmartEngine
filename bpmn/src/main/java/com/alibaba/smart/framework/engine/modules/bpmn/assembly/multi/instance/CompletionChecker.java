package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance;

import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Performable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:27.
 */
@Data
public class CompletionChecker implements BaseElement {
    private boolean custom;

    private Performable completionCheckPerformable;

    private Performable abortCheckPerformable;
}

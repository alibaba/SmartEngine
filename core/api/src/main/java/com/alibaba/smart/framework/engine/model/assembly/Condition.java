package com.alibaba.smart.framework.engine.model.assembly;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Condition extends AbstractBase {

    private static final long serialVersionUID = 1246008761760333354L;
    private Handler handler;

}

package com.alibaba.smart.framework.engine.model.assembly;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by ettear on 16-4-29.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Condition extends AbstractBase {

    private static final long serialVersionUID = 1246008761760333354L;
    private Handler handler;

}

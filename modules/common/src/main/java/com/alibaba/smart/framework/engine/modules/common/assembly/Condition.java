package com.alibaba.smart.framework.engine.modules.common.assembly;

import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.assembly.impl.AbstractBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by ettear on 16-4-29.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Condition extends AbstractBase{
    private Handler handler;

}

package com.alibaba.smart.framework.engine.modules.common.assembly;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.assembly.impl.AbstractBase;

/**
 * Created by ettear on 16-4-29.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Condition extends AbstractBase {

    private Handler handler;

}

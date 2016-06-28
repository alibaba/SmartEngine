package com.alibaba.smart.framework.engine.assembly;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractBase;

/**
 * Script Created by ettear on 16-4-29.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Script extends AbstractBase implements Handler {

    private String type;
    private String content;
    private String resultVariable;
}

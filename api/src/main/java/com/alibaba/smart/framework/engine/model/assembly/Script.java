package com.alibaba.smart.framework.engine.model.assembly;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBaseElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Deprecated
public class Script extends AbstractBaseElement implements BaseElement {

    private static final long serialVersionUID = -5323003411376547684L;
    private String type;
    private String content;
    private String resultVariable;
}

package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.ExtensionBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionContainer;

import lombok.Data;

/**
 * @author pengziran
 * Created by pengziran on 01/08/2017.
 */
@Data
public abstract class AbstractElement implements ExtensionBasedElement {

    //TUNE add sid

    private static final long serialVersionUID = -8729383608303781741L;

    private String id;
    private String name;
    private ExtensionContainer extensionContainer;
}

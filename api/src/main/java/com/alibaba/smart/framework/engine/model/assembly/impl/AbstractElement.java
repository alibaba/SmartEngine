package com.alibaba.smart.framework.engine.model.assembly.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.model.assembly.Element;
import com.alibaba.smart.framework.engine.model.assembly.Extensions;
import com.alibaba.smart.framework.engine.model.assembly.Performable;

import lombok.Data;

/**
 * @author pengziran
 * Created by pengziran on 01/08/2017.
 */
@Data
public abstract class AbstractElement implements Element {

    private static final long serialVersionUID = -8729383608303781741L;
    private boolean unresolved = true;
    private String id;
    private Extensions extensions;
    private List<Performable> performers;
}

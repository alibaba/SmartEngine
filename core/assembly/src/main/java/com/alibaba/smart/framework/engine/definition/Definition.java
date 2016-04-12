package com.alibaba.smart.framework.engine.definition;

import com.alibaba.smart.framework.engine.assembly.Base;

import java.util.List;

/**
 * Created by ettear on 16-4-11.
 */
public interface Definition {

    Long getId();

    String getName();

    Integer getVersion();

    List<Base> getNodes();
}

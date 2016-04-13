package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.Activity;

/**
 * Created by ettear on 16-4-12.
 */
public interface RuntimeProcess extends RuntimeActivity {
    String getId();

    String getName();

    String getVersion();
}

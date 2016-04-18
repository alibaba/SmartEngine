package com.alibaba.smart.framework.engine.instance;

import java.util.List;

/**
 * Created by ettear on 16-4-18.
 */
public interface ActivityInstance {
    String id();
    String getName();
    List<String> getSources();
    TaskInstance getTask();
}

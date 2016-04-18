package com.alibaba.smart.framework.engine.instance;

import java.util.List;

/**
 * Created by ettear on 16-4-12.
 */
public interface Instance {
    String getId();
    List<Task> getTasks();
    List<Status> getStatus();
}

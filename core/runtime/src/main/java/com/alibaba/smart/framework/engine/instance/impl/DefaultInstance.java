package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.Instance;
import com.alibaba.smart.framework.engine.instance.Status;
import com.alibaba.smart.framework.engine.instance.Task;

import java.util.List;

/**
 * Created by ettear on 16-4-12.
 */
public class DefaultInstance implements Instance{

    @Override
    public String getId() {
        return null;
    }

    @Override
    public List<Task> getTasks() {
        return null;
    }

    @Override
    public List<Status> getStatus() {
        return null;
    }
}

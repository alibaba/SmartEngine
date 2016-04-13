package com.alibaba.smart.framework.engine.instance;

import java.util.List;

/**
 * Created by ettear on 16-4-13.
 */
public interface Status {
    String getStatus();
    List<Status> getSubStatus();
}

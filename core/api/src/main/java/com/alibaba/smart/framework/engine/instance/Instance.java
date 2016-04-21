package com.alibaba.smart.framework.engine.instance;

import java.io.Serializable;
import java.util.Date;

/**
 * 实例
 * Created by ettear on 16-4-19.
 */
public interface Instance extends Serializable {

    /**
     * 获取实例ID
     * @return 实例ID
     */
    String getInstanceId();

    /**
     * 设置实例ID
     * @param id 实例ID
     */
    void setInstanceId(String id);
}

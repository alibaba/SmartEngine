package com.alibaba.smart.framework.engine.model.instance;

import java.io.Serializable;

/**
 * 实例 Created by ettear on 16-4-19.
 */
public interface Instance extends Serializable {

    /**
     * 获取实例ID
     *
     * @return 实例ID
     */

    //TUNE 改成id好了
    Long getInstanceId();

    /**
     * 设置实例ID
     *
     * @param id 实例ID
     */
    void setInstanceId(Long id);
}

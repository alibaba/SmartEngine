package com.alibaba.smart.framework.engine.assembly;

/**
 * 可执行元素 Created by ettear on 16-4-11.
 */
public interface Invocable extends Base {

    /**
     * 获取元素ID
     * 
     * @return 元素ID
     */
    String getId();

    void setId(String id);
}

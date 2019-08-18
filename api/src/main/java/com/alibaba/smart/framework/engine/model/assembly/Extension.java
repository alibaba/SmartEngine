package com.alibaba.smart.framework.engine.model.assembly;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */

public interface Extension extends BaseElement {
    /**
     * 是否为准备阶段的扩展，准备阶段扩展会在行为之前执行
     * @return 是否为准备阶段的扩展
     */
    boolean isPrepare();
}

package com.alibaba.smart.framework.engine.extensionpoint;

import com.alibaba.smart.framework.engine.hook.LifeCycleHook;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ExtensionPointRegistry extends LifeCycleHook {

    void register();

    /**
     * 获取扩展点
     *
     * @param extensionPointType 扩展点类型
     * @return 扩展点
     */
    <T> T getExtensionPoint(Class<T> extensionPointType);

}

package com.alibaba.smart.framework.engine.extensionpoint.registry;

import com.alibaba.smart.framework.engine.listener.LifeCycleListener;


/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ExtensionPointRegistry extends LifeCycleListener {

    void register(String moduleName, ClassLoader classLoader);

    /**
     * 获取扩展点
     *
     * @param extensionPointType 扩展点类型
     * @return 扩展点
     */
    <T> T getExtensionPoint(Class<T> extensionPointType);

}

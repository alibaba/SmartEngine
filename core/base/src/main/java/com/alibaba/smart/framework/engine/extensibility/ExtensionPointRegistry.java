package com.alibaba.smart.framework.engine.extensibility;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;

/**
 * 扩展点注册器
 * Created by ettear on 16-4-12.
 */
public interface ExtensionPointRegistry extends LifeCycleListener {

    /**
     * 获取扩展点
     *
     * @param extensionPointType 扩展点类型
     * @return 扩展点
     */
    <T> T getExtensionPoint(Class<T> extensionPointType);
}

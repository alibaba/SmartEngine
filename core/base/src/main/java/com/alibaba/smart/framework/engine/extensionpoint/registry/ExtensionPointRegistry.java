package com.alibaba.smart.framework.engine.extensionpoint.registry;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.extensionpoint.registry.exception.ExtensionPointLoadException;

/**
 * Class Loader Extension Point Created by ettear on 16-4-12.
 */
public interface ExtensionPointRegistry  extends LifeCycleListener {

    void load(String moduleName, ClassLoader classLoader) throws ExtensionPointLoadException;
    
    /**
     * 获取扩展点
     *
     * @param extensionPointType 扩展点类型
     * @return 扩展点
     */
    <T> T getExtensionPoint(Class<T> extensionPointType);

}

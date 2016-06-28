package com.alibaba.smart.framework.engine.extensibility;

import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;

/**
 * Class Loader Extension Point Created by ettear on 16-4-12.
 */
public interface ClassLoaderExtensionPoint {

    void load(String moduleName, ClassLoader classLoader) throws ExtensionPointLoadException;

}

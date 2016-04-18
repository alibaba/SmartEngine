package com.alibaba.smart.framework.engine.runtime;

/**
 * Created by ettear on 16-4-13.
 */
public interface RuntimeProcessComponent {
    String getId();
    String getVersion();
    ClassLoader getClassLoader();
    RuntimeProcess getProcess();

}

package com.alibaba.smart.framework.engine.instance.util;


/**
 * Created by ettear on 16-4-19.
 */
public abstract class InstanceIdUtil {

    private static Long executionId = 1L;


    //TODO DELETE
    public static Long simpleId() {

        return (executionId++);

    }

}

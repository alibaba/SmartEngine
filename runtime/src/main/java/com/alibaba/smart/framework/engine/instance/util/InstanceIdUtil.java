package com.alibaba.smart.framework.engine.instance.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by ettear on 16-4-19.
 */
public abstract class InstanceIdUtil {

    private static Long executionId = 1L;

    public static String uuid() {
        return StringUtils.remove(UUID.randomUUID().toString(), "-");
    }


    //TODO DELETE
    public static Long simpleId() {

        return (executionId++);

    }

}

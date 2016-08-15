package com.alibaba.smart.framework.engine.instance.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ettear on 16-4-19.
 */
public abstract class InstanceIdUtil {

    private static int executionId = 1;

    public static String uuid() {
        return StringUtils.remove(UUID.randomUUID().toString(), "-");
    }


    public static String simpleId() {

        return String.valueOf(executionId++);

    }

}

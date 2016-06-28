package com.alibaba.smart.framework.engine.instance.utils;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ettear on 16-4-19.
 */
public abstract class InstanceIdUtils {

    public static String uuid() {
        return StringUtils.remove(UUID.randomUUID().toString(), "-");
    }

}

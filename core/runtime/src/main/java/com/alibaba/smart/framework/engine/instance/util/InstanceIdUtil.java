package com.alibaba.smart.framework.engine.instance.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ettear on 16-4-19.
 */
public abstract class InstanceIdUtil {

    public static String uuid() {
        return StringUtils.remove(UUID.randomUUID().toString(), "-");
    }

}

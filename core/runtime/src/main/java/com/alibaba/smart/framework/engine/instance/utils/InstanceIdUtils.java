package com.alibaba.smart.framework.engine.instance.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by ettear on 16-4-19.
 */
public abstract class InstanceIdUtils {
    public static String uuid(){
        return StringUtils.remove(UUID.randomUUID().toString(),"-");
    }

}

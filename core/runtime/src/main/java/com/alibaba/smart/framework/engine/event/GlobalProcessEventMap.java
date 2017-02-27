package com.alibaba.smart.framework.engine.event;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * @author dongdong.zdd
 * @since 2017-02-27
 */
public class GlobalProcessEventMap {

    private static Map<String,ProcessEventMap> globalProcessEventMap = Maps.newHashMap();


    public static Map<String, ProcessEventMap> globalMap() {
        return globalProcessEventMap;
    }
}

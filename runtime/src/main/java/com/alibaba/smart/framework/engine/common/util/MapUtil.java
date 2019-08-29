package com.alibaba.smart.framework.engine.common.util;

import java.util.Map;

public class MapUtil {
    public static boolean isNotEmpty(Map map) {
        return null != map && !map.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return !isNotEmpty(map);
    }
}
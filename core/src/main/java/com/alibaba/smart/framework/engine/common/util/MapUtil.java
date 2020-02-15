package com.alibaba.smart.framework.engine.common.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtil {

    public static LinkedHashMap newLinkedHashMap (){
        return new LinkedHashMap();
    }

    public static HashMap newHashMap (){
        return new HashMap();
    }

    public static boolean isNotEmpty(Map map) {
        return null != map && !map.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return !isNotEmpty(map);
    }
}
package com.alibaba.smart.framework.engine.common.util;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  10:23.
 */
public abstract  class StringUtil {

    public static boolean equals(String a, String b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    public static boolean isEmpty(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }

        return false;
    }
}


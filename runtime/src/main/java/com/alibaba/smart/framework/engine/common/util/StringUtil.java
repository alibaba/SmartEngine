//package com.alibaba.smart.framework.engine.common.util;
//
///**
// * Created by 高海军 帝奇 74394 on 2017 February  10:23.
// */
//public abstract  class StringUtil {
//
//    public static boolean equals(String a, String b) {
//        if (a == null) {
//            return b == null;
//        }
//        return a.equals(b);
//    }
//
//    public static boolean isEmpty(String value) {
//        if (value == null || value.length() == 0) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public static boolean isNotEmpty(String value) {
//        return !isEmpty(value);
//    }
//
//    public static String removeEnd(final String str, final String remove) {
//        if (isEmpty(str) || isEmpty(remove)) {
//            return str;
//        }
//        if (str.endsWith(remove)) {
//            return str.substring(0, str.length() - remove.length());
//        }
//        return str;
//    }
//    public static String removeStart(final String str, final String remove) {
//        if (isEmpty(str) || isEmpty(remove)) {
//            return str;
//        }
//        if (str.startsWith(remove)){
//            return str.substring(remove.length());
//        }
//        return str;
//    }
//}
//

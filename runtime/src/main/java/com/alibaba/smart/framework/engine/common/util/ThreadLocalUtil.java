//package com.alibaba.smart.framework.engine.common.util;
//
//import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
//
///**
// * @author 高海军 帝奇
// */
//public class ThreadLocalUtil {
//
//    private static ProcessEngineConfiguration holder ;
//
//    public static void set(ProcessEngineConfiguration value) {
//        if (holder == null) {
//            holder = value;
//        }
//    }
//
//    public static void remove() {
//        holder = null;
//    }
//
//    public static ProcessEngineConfiguration get() {
//        return holder;
//    }
//
//}

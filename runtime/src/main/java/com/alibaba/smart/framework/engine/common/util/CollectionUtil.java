package com.alibaba.smart.framework.engine.common.util;

import java.util.Collection;

/**
 * Created by 高海军 帝奇 74394 on 2018 November  16:20.
 */
public  abstract class CollectionUtil {

    public static boolean isNotEmpty(Collection collection){
        return null != collection && !collection.isEmpty();
    }


    public static boolean isEmpty(Collection collection){
        return !isNotEmpty(collection);
    }

}
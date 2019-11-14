package com.alibaba.smart.framework.engine.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by 高海军 帝奇 74394 on 2018 November  16:20.
 */
public  abstract class CollectionUtil {

    public static List newArrayList (){
        return new ArrayList();
    }


    public static boolean isNotEmpty(Collection collection){
        return null != collection && !collection.isEmpty();
    }

    public static boolean isEmpty(Collection collection){
        return !isNotEmpty(collection);
    }

}
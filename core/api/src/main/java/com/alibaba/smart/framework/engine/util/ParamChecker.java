/**
 * 
 */
package com.alibaba.smart.framework.engine.util;

import java.util.Collection;

/**
 * 参数校验帮助类
 * 
 * @author dongdong.zdd
 *
 */
public class ParamChecker {
	
	 /**
     * Check that a value is not null. If null throws an IllegalArgumentException.
     *
     * @param obj value.
     * @param name parameter name for the exception message.
     * @return the given value.
     */
    public static <T> T notNull(T obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
        return obj;
    }
    
    @SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Collection coll) {
        return !ParamChecker.isEmpty(coll);
    }
    
    @SuppressWarnings("rawtypes")
	public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

}

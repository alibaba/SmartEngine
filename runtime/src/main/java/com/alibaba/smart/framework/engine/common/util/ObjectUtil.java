package com.alibaba.smart.framework.engine.common.util;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ObjectUtil
 * <pre>
 * 对象转换工具
 *  </pre>
 *
 * @author xiuqun.hxq@alibaba-inc.com
 * @date 2019/3/13 下午8:12  03月 第 13天
 */
public class ObjectUtil {

    public static <T> Map<String,Object> parse2Map(T t){
        Map<String,Object> result=new HashMap<String, Object>();
        if(t==null){
            return result;
        }
        if(t instanceof List){
            throw new RuntimeException("no support");
        }
        if(t instanceof Map){
            result.putAll((Map<String,Object>) t);
            return result;
        }
        //使用反射将对象转换为map
        Field [] fields=t.getClass().getDeclaredFields();
        try {
            for(Field field:fields){
                String key=field.getName();
                field.setAccessible(true);
                Object value=field.get(t);
                result.put(key,value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Data
    static class Person{
        private String name;
        private Integer age;
    }

//    public static void main(String[] args){
//            Person person=new Person();
//            person.setName("hxq");
//            person.setAge(18);
//            Map<String,Object> map=parse2Map(person);
//    }
}

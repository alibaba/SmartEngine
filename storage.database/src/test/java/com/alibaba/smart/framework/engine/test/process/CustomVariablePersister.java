package com.alibaba.smart.framework.engine.test.process;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  07:00.
 */
public class CustomVariablePersister implements VariablePersister {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomVariablePersister.class);


    private static  Set<String> hashSet = new HashSet();

   static {

       try {
           RequestMapSpecialKeyConstant constant =  RequestMapSpecialKeyConstant.class.newInstance();
           Field[] declaredFields = RequestMapSpecialKeyConstant.class.getDeclaredFields();
           for (Field declaredField : declaredFields) {
               String key= (String)declaredField.get(declaredField.getName());
               hashSet.add(key);
           }
       } catch (InstantiationException e) {
           LOGGER.error(e.getMessage(),e);
       } catch (IllegalAccessException e) {
           LOGGER.error(e.getMessage(),e);
       }

        //do something else.
       //hashSet.add("text");
   }

    @Override
    public boolean isPersisteVariableInstanceEnabled() {
        return true;
    }



    @Override
    public Set<String> getBlackList() {


        return hashSet;
    }

    @Override
    public String serialize(Object value) {
        return JSON.toJSONString(value);
    }

    @Override
    public <T> T deserialize(String text, Class<T> clazz) {
        return  JSON.parseObject(text,clazz);
    }
}

package com.alibaba.smart.framework.engine.modules.bpmn;

import java.util.Map;

/**
 * Created by dongdongzdd on 16/9/8.
 */
public class CreateOrder {

    public void create(Map<String, Object> context) {
        System.out.println("\"run success!!!!!!!!!!!!!);");
        context.entrySet().stream().forEach(p -> System.out.println(p.toString()));
    }


}

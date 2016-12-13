package com.alibaba.smart.framework.engine.test;

import java.util.Map;

/**
 * @author dongdong.zdd
 * @since 2016-12-13
 */
public class TestAbortEvent {

    public void process(Map<String,Object> context) {
        System.out.println("\"run success!!!!!!!!!!!!!);");
        context.entrySet().stream().forEach(p -> System.out.println(p.toString()));
    }

}

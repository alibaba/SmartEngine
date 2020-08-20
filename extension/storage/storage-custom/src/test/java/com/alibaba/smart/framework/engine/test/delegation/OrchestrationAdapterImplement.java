package com.alibaba.smart.framework.engine.test.delegation;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class OrchestrationAdapterImplement implements  OrchestrationAdapter {

     public  static  AtomicLong counter = new AtomicLong(0);

     public static void resetCounter() {
          counter.set(0L);
     }


     @Override
     public void execute(Map<String, Object> request, Map<String, Object> response) {
          counter.addAndGet(1L);
     }
}
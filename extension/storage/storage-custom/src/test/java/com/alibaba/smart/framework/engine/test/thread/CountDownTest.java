package com.alibaba.smart.framework.engine.test.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by 高海军 帝奇 74394 on  2020-08-10 14:45.
 */
public class CountDownTest {

    public static void main(String[] args) throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(2);
        long start = System.currentTimeMillis();

        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1200L);
                    System.out.println("await1");

                    latch.countDown();
                    //latch.await();
                    //System.out.println("endt1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();


        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2200L);

                    System.out.println("await2");

                    //latch.await();
                    //System.out.println("endt2");
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t2.start();


        latch.await();
        latch.await();
        //latch.await();

        System.out.println("here");

        long end = System.currentTimeMillis();

        long duration = end-start;

        System.out.println("duration"+duration);


    }
}
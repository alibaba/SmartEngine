package com.alibaba.smart.framework.engine.common.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhenhong.tzh
 * @date 2019-06-28
 */
public class ThreadPoolUtil {

    private static int CORE_THREAD_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 3;

    private static int BUFFER_SIZE = 50;

    private static long KEEP_ALIVE_TIME = 1000L;

    /**
     * 默认线程池超时
     */
    public volatile static long TIME_OUT = 50000000000L;

    private static class DefaultThreadFactory implements ThreadFactory {

        final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
        final String namePrefix;

        DefaultThreadFactory(String poolName) {
            namePrefix = "ENGINE-" + poolName + "-";
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(null, r, namePrefix + THREAD_NUMBER.getAndIncrement());
        }
    }

    public static ExecutorService createNewDefaultThreadPool(String poolName) {
        return new ThreadPoolExecutor(CORE_THREAD_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(BUFFER_SIZE),
            new DefaultThreadFactory(poolName), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}

package com.alibaba.smart.framework.engine.util;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author guoxing
 * @date 2021/1/14
 * @description 描述
 */
public class CompletedFuture<T> implements Future<T> {
    private final T v;
    private final Throwable re;

    public CompletedFuture(T v, Throwable re) {
        this.v = v;
        this.re = re;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return this.v == null && this.re != null;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public T get() throws CancellationException {
        // 将异常转化为CancellationException， 兼容ExecutorService.invokeAll的模式
        if (this.re != null) {
            throw new CancellationException(this.re.getMessage());
        } else {
            return this.v;
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws CancellationException {
        return this.get();
    }
}

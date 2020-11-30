package com.alibaba.smart.framework.engine.constant;

/**
 * 并行网关扩展属性常量
 *
 * @author guoxing
 * @date 2020年11月16日19:29:08
 */
public final class ParallelGatewayConstant {

   /**
    * private scope constructor for avoid new instance
    */
   private ParallelGatewayConstant() {
   }

   /**
    * 自定义属性名：等待超时时间，单位毫秒。
    */
   public static final String WAIT_TIME_OUT = "timeout";
   /**
    * 自定义线程池名称
    */
   public static final String POOL_NAME = "poolName";
   /**
    * 是否超时异常时跳过
    */
   public static final String SKIP_TIMEOUT_EXCEPTION = "skipTimeoutExp";


}

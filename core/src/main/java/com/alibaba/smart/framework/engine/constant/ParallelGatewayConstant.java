package com.alibaba.smart.framework.engine.constant;

import lombok.Getter;

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
    * 当超时异常时,是否跳过
    */
   public static final String SKIP_TIMEOUT_EXCEPTION = "skipTimeoutExp";

   /**
    * 执行策略，value为code对应的数字
    */
   public static final String EXE_STRATEGY = "strategy";


   public enum ExecuteStrategy {
      /**
       * 执行所有
       */
      INVOKE_ALL("all", "执行所有"),
      /**
       * 返回最快的那个，中断其他进行中的并行线程
       */
      INVOKE_ANY("any", "返回最快的任意一个")
      ;

      /**
       * 1 表示执行所有
       * 2 表示执行最快的那个，类似race模式
       */
      @Getter
      private final String code;
      /**
       * 策略的描述
       */
      @Getter
      private final String desc;

      ExecuteStrategy(String code, String desc) {
         this.code = code;
         this.desc = desc;
      }


      /**
       * 返回一个执行策略
       * @param code
       * @return
       */
      public static ExecuteStrategy build(String code) {
         ExecuteStrategy[] strategies = ExecuteStrategy.values();
         for (ExecuteStrategy strategy : strategies) {
            if (strategy.getCode().equalsIgnoreCase(code)) {
               return strategy;
            }
         }
         return null;
      }

   }
}

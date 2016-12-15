package com.alibaba.smart.framework.engine.common.util;

/**
 * Created by 高海军 帝奇 74394 on 2016 December  16:12.
 */
public abstract class IdAndVersion {

  public static String  buildIdAndVersion(String processDefinitionId,String version){
      return processDefinitionId+":"+version;
  }



}

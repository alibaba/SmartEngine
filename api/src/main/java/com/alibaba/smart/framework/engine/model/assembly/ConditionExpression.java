package com.alibaba.smart.framework.engine.model.assembly;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
public interface ConditionExpression extends NoneIdBasedElement {

      String getExpressionType();
      void setExpressionType(String type);

      String getExpressionContent();
      void setExpressionContent(String content);


}

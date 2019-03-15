package com.alibaba.smart.framework.engine.service.param.query.condition;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * OperatorType
 * <pre>
 * 操作符枚举，只能使用内置操作符，不能用户自定义
 *  </pre>
 *
 * @author xiuqun.hxq@alibaba-inc.com
 * @date 2019/3/14 下午8:23  03月 第 14天
 */
public enum  OperatorType {

    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    E("="),
    NE("!="),
    LIKE("like");


    private static Map<String,OperatorType> operatorTypeMap=new HashMap<String, OperatorType>();
    static {
        for(OperatorType operatorType:OperatorType.values()){
            operatorTypeMap.put(operatorType.operator,operatorType);
        }
    }

    /**
     * 操作符
     */
    public String operator;

    OperatorType(String operator){
        this.operator=operator;
    }

    /**
     * 判断操作符是否有效
     * @param operator
     * @return
     */
    public static boolean valid(String operator){
        return operatorTypeMap.get(operator)!=null;
    }
}

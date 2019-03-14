package com.alibaba.smart.framework.engine.service.param.query.condition;

import java.util.HashMap;
import java.util.Map;

/**
 * ConditionType
 * <pre>
 * 条件类型,内定2个值，不允许用户自定义
 *  </pre>
 *
 * @author xiuqun.hxq@alibaba-inc.com
 * @date 2019/3/14 下午8:32  03月 第 14天
 */
public enum ConditionType {

   AND("and"), OR("or");


    private String type;

    private static final Map<String,ConditionType> conditionTypeMap=new HashMap<String, ConditionType>();
    static {
        for(ConditionType conditionType:ConditionType.values()){
            conditionTypeMap.put(conditionType.type,conditionType);
        }
    }

    ConditionType(String type){
        this.type=type;
    }

    public static boolean valid(String type){
        return conditionTypeMap.get(type)!=null;
    }
}

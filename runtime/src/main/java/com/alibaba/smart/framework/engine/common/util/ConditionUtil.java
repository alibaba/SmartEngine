package com.alibaba.smart.framework.engine.common.util;

import com.alibaba.smart.framework.engine.service.param.query.condition.CustomFieldCondition;

import java.util.List;

/**
 * ConditionUtil
 * <pre>
 * 判断条件是否有效
 *  </pre>
 *
 * @author xiuqun.hxq@alibaba-inc.com
 * @date 2019/3/14 下午9:03  03月 第 14天
 */
public class ConditionUtil {

    /**
     * 判断字段是否
     * @param customFieldConditionList
     * @return
     */
    public static boolean isValid(List<CustomFieldCondition> customFieldConditionList){
        if(customFieldConditionList==null||customFieldConditionList.size()==0){
            return true;
        }
        for(CustomFieldCondition customFieldCondition:customFieldConditionList){
            if(!customFieldCondition.isValid()){
                return false;
            }
        }
        return true;
    }
}

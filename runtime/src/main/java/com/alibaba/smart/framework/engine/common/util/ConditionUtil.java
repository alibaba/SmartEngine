package com.alibaba.smart.framework.engine.common.util;

import com.alibaba.smart.framework.engine.service.param.query.CustomFieldsQueryParam;
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
     * @param customFieldsQueryParam
     * @return
     */
    public static boolean isValid(CustomFieldsQueryParam customFieldsQueryParam){
        if(customFieldsQueryParam==null||customFieldsQueryParam.getCustomFieldConditionList()==null||customFieldsQueryParam.getCustomFieldConditionList().size()==0){
            return true;
        }
        for(CustomFieldCondition customFieldCondition:customFieldsQueryParam.getCustomFieldConditionList()){
            if(!customFieldCondition.isValid()){
                return false;
            }
        }
        return true;
    }
}

package com.alibaba.smart.framework.engine.service.param.query;

import com.alibaba.smart.framework.engine.service.param.query.condition.CustomFieldCondition;
import lombok.Data;

import java.util.List;

/**
 * CustomFieldsQueryParam
 * <pre>
 * 自定义字段查询
 *  </pre>
 *
 * @author xiuqun.hxq@alibaba-inc.com
 * @date 2019/3/18 下午10:16  03月 第 18天
 */
@Data
public class CustomFieldsQueryParam {


    /**
     * 所有的动态查询条件，用于搜索
     */
    private List<CustomFieldCondition> customFieldConditionList;

    /**
     * 所以需要返回的自定义字段列表
     */
    private List<String> allCustomFieldsList;
}

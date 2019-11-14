package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on  2019-11-14 16:37.
 */
@Data
public class EventListenerAggregation {

    private Map<String, List<JavaDelegation>> eventListenerMap = MapUtil.newHashMap();

}
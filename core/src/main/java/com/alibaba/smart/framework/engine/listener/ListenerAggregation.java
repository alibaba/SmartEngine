package com.alibaba.smart.framework.engine.listener;

import com.alibaba.smart.framework.engine.common.util.MapUtil;

import lombok.Data;

import java.util.List;
import java.util.Map;

/** Created by 高海军 帝奇 74394 on 2019-11-14 16:37. */
@Data
public class ListenerAggregation {

    private Map<String, List<String>> eventListenerMap = MapUtil.newHashMap();
}

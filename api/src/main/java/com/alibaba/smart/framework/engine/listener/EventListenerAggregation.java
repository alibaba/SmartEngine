package com.alibaba.smart.framework.engine.listener;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.MapUtil;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on  2019-11-14 16:37.
 */
@Data
public class EventListenerAggregation {

    private Map<String, List<EventListener>> eventListenerMap = MapUtil.newHashMap();

}
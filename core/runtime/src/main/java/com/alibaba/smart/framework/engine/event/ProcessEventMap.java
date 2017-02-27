package com.alibaba.smart.framework.engine.event;

import com.google.common.collect.Maps;
import lombok.Setter;

import java.util.Map;

/**
 * @author dongdong.zdd
 * @since 2017-02-27
 */
public class ProcessEventMap {


    @Setter
    private String flowId;


    private Map<String,String> eventMap = Maps.newHashMap();

    public void setEventAndActivityMap(String evnet,String activity) {
        this.eventMap.put(activity,evnet);
    }


    public  String getKeybyValue(String value) {
        if (eventMap.isEmpty()) {
            return null;
        }
        return eventMap.values().stream().filter(p->value.equals(p)).findFirst().orElse(null);

    }
}

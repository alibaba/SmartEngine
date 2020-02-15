package com.alibaba.smart.framework.engine.common.util;

/**
 * Created by 高海军 帝奇 74394 on  2019-11-17 15:55.
 */
public abstract class IdAndVersionBuilder {

    public static String buildProcessDefinitionKey(String processDefinitionId, String version) {

        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(processDefinitionId);
        uriBuilder.append(":").append(version);

        return uriBuilder.toString();
    }

}
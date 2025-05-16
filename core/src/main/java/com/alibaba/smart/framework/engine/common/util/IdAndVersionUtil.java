package com.alibaba.smart.framework.engine.common.util;

/**
 * Created by 高海军 帝奇 74394 on  2019-11-17 15:55.
 */
public abstract class IdAndVersionUtil {

    public static String buildProcessDefinitionKey(String processDefinitionId, String version) {

        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(processDefinitionId);
        uriBuilder.append(":").append(version);

        return uriBuilder.toString();
    }

    public static String buildProcessDefinitionUniqueKey(String processDefinitionId, String version,String tenantId) {

        if(StringUtil.isEmpty(tenantId)){
            return buildProcessDefinitionKey(processDefinitionId, version);
        }else {
            StringBuilder uriBuilder = new StringBuilder(buildProcessDefinitionKey(processDefinitionId, version));
            uriBuilder.append(":").append(tenantId);
            return uriBuilder.toString();
        }
    }

}
package com.alibaba.smart.framework.engine.configuration.impl.option;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;

/**
 * Sharding mode configuration option.
 * When enabled, index tables (se_user_task_index, se_user_notification_index) will be maintained,
 * and queries without sharding key (processInstanceId) will throw ShardingKeyRequiredException.
 */
public class ShardingModeEnabledOption implements ConfigurationOption {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getId() {
        return "shardingModeEnabled";
    }

    @Override
    public Object getData() {
        return null;
    }
}

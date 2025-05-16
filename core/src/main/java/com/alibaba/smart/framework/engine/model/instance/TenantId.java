package com.alibaba.smart.framework.engine.model.instance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 租户ID
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TenantId {
    private String value;
}

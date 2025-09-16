package com.alibaba.smart.framework.engine.delegation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 高海军 帝奇 2016.11.11
 */
@Deprecated
public class TccResult {

    private TccResult() {}

    @Getter @Setter private boolean isSucessful;

    @Getter @Setter private Object target;

    public static TccResult buildSucessfulResult(Object target) {
        TccResult result = new TccResult();
        result.setSucessful(true);
        result.setTarget(target);
        return result;
    }

    public static TccResult buildFailedResult(Object target) {
        TccResult result = new TccResult();
        result.setSucessful(false);
        result.setTarget(target);
        return result;
    }
}

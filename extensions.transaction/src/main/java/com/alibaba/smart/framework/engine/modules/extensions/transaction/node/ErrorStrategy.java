package com.alibaba.smart.framework.engine.modules.extensions.transaction.node;


/**
 * @author Leo.yy   Created on 2017/8/2.
 * @description
 * @see
 */
public enum ErrorStrategy {

    ROLLBACK("rollback"),
    REDO("redo"),
    EMPTY("empty");

    private String name;

    ErrorStrategy(String name) {
        this.name = name;
    }


    public static ErrorStrategy getByName(String name) {
        for (ErrorStrategy strategy : values()) {
            if (strategy.name.equals(name)) {
                return strategy;
            }
        }

        return null;
    }

}

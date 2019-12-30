package com.alibaba.smart.framework.engine.configuration.scanner;

import java.util.Map;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on  2019-08-27 14:32.
 */

@Data
public class ExtensionBindingResult {

    private Map<Class,Object> bindingMap;

}
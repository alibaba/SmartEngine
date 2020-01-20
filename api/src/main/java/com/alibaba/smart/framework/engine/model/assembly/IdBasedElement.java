package com.alibaba.smart.framework.engine.model.assembly;

import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface IdBasedElement extends BaseElement {

    String getId();

    void setId(String id);

    Map<String,String> getProperties();

}

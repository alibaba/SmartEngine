package com.alibaba.smart.framework.engine.model.assembly;

import java.util.List;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface Process extends NoneExtensionBasedElement {

    List<BaseElement> getElements();
}

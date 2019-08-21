package com.alibaba.smart.framework.engine.model.assembly;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
public class Extensions implements BaseElement {

    private static final long serialVersionUID = -9017389195653634752L;
    private List<Extension> extensions = new ArrayList<Extension>(5);

    public void addExtension(Extension extension) {
        this.extensions.add(extension);
    }
}

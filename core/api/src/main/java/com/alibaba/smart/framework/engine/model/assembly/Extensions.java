package com.alibaba.smart.framework.engine.model.assembly;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBase;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Extensions extends AbstractBase {

    private static final long serialVersionUID = -9017389195653634752L;
    private List<Extension>   extensions       = new ArrayList<Extension>(5);

    public void addExtension(Extension extension) {
        this.extensions.add(extension);
    }
}

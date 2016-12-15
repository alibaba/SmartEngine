package com.alibaba.smart.framework.engine.model.assembly;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ettear on 16-4-12.
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

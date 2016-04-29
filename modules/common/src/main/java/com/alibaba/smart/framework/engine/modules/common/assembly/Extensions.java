package com.alibaba.smart.framework.engine.modules.common.assembly;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by ettear on 16-4-12.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Extensions extends AbstractBase {

    private List<Extension> extensions;

    public void addExtension(Extension extension) {
        this.extensions.add(extension);
    }
}

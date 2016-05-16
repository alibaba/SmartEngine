package com.alibaba.smart.framework.engine.modules.common.assembly;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractBase;

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

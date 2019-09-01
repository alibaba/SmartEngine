package com.alibaba.smart.framework.engine.model.assembly;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
public class Extensions implements BaseElement {

    private List<Extension> extensions = new ArrayList<Extension>();
    private Map<String,String> properties = new HashMap<String, String>();
    private Map<String,List<String >> eventListeners = new HashMap<String, List<String >>();


    public void addExtension(Extension extension) {
        extension.decorate(this);
        this.extensions.add(extension);
    }
}

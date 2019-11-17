package com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ettear on 16-4-29.
 */
@Data
public class ExtensionElementsImpl implements ExtensionElements {

    private static final long serialVersionUID = -5080932640599337544L;
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "extensionElements");

    private List<Extension> extensionList = new ArrayList<Extension>();

    private Map<String,Object> decorationMap = new HashMap<String, Object>();

    @Override
    public void decorate(Extension extension) {

        this.extensionList.add(extension);

        extension.decorate(this);

    }


}

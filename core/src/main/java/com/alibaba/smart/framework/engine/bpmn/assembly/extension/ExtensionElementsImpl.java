package com.alibaba.smart.framework.engine.bpmn.assembly.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import lombok.Data;

/**
 * Created by ettear on 16-4-29.
 */
@Data
public class ExtensionElementsImpl implements ExtensionElements {

    private static final long serialVersionUID = -5080932640599337544L;
    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "extensionElements");

    private List<ExtensionDecorator> extensionList = new ArrayList<ExtensionDecorator>();

    private Map<String,Object> decorationMap = new HashMap<String, Object>();

    @Override
    public void decorate(ExtensionDecorator extension, ParseContext context) {

        this.extensionList.add(extension);

        extension.decorate(this, context);

    }


}

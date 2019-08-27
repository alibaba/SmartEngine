package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartProcess;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = SmartProcess.class)

public class SmartProcessParser extends AbstractElementParser<SmartProcess> {

    public SmartProcessParser(ExtensionPointRegistry extensionPointRegistry) {

        super(extensionPointRegistry);
    }

    @Override
    protected SmartProcess parseModel(XMLStreamReader reader, ParseContext context) {
        SmartProcess smartProcess = new SmartProcess();
        smartProcess.setId(XmlParseUtil.getString(reader, "id"));
        return smartProcess;
    }

    @Override
    protected void parseSingleChild(SmartProcess smartProcess, BaseElement child) {
        List<BaseElement> elements = smartProcess.getElements();
        if (null == elements) {
            elements = new ArrayList<BaseElement>();
            smartProcess.setElements(elements);
        }
        elements.add(child);

    }

    @Override
    public QName getQname() {
        return SmartProcess.type;
    }

    @Override
    public Class<SmartProcess> getModelType() {
        return SmartProcess.class;
    }

}

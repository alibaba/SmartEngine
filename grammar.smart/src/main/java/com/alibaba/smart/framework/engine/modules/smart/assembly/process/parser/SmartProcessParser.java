package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartProcess;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class SmartProcessParser extends AbstractElementParser<SmartProcess> {

    public SmartProcessParser(ExtensionPointRegistry extensionPointRegistry) {

        super(extensionPointRegistry);
    }

    @Override
    protected SmartProcess parseModel(XMLStreamReader reader, ParseContext context) {
        SmartProcess smartProcess = new SmartProcess();
        smartProcess.setId(this.getString(reader, "id"));
        return smartProcess;
    }

    @Override
    protected void parseChild(SmartProcess smartProcess, BaseElement child) {
        List<BaseElement> elements = smartProcess.getElements();
        if (null == elements) {
            elements = new ArrayList<BaseElement>();
            smartProcess.setElements(elements);
        }
        elements.add(child);

    }

    @Override
    public QName getArtifactType() {
        return SmartProcess.type;
    }

    @Override
    public Class<SmartProcess> getModelType() {
        return SmartProcess.class;
    }

}
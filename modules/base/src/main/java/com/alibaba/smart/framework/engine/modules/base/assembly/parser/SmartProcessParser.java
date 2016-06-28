package com.alibaba.smart.framework.engine.modules.base.assembly.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ResolveException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartProcess;

/**
 * SmartProcessParser Created by ettear on 16-4-14.
 */
public class SmartProcessParser extends AbstractStAXArtifactParser<SmartProcess> implements StAXArtifactParser<SmartProcess> {

    public SmartProcessParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public SmartProcess parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        SmartProcess smartProcess = new SmartProcess();
        smartProcess.setId(this.getString(reader, "id"));

        List<Base> elements = new ArrayList<>();
        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof Base) {
                elements.add((Base) element);
            }
        }
        smartProcess.setElements(elements);
        return smartProcess;
    }

    @Override
    public void resolve(SmartProcess model, ParseContext context) throws ResolveException {
        if (null != model.getElements()) {
            for (Base element : model.getElements()) {
                this.resolveElement(element, context);
            }
        }
        model.setUnresolved(false);
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

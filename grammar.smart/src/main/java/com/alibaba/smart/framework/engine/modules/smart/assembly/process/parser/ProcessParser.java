package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.Process;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class ProcessParser extends AbstractElementParser<Process> {

    public ProcessParser(ExtensionPointRegistry extensionPointRegistry) {

        super(extensionPointRegistry);
    }

    @Override
    protected Process parseModel(XMLStreamReader reader, ParseContext context) {
        Process process = new Process();
        process.setId(this.getString(reader, "id"));
        return process;
    }

    @Override
    protected void parseChild(Process process, BaseElement child) {
        List<BaseElement> elements = process.getElements();
        if (null == elements) {
            elements = new ArrayList<BaseElement>();
            process.setElements(elements);
        }
        elements.add(child);

    }

    @Override
    public QName getArtifactType() {
        return Process.type;
    }

    @Override
    public Class<Process> getModelType() {
        return Process.class;
    }

}

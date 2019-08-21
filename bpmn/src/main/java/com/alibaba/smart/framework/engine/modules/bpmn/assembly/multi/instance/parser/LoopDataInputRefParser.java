package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopDataInputRef;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.ElementParser;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class LoopDataInputRefParser extends AbstractElementParser<LoopDataInputRef>
      {

    public LoopDataInputRefParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getQname() {
        return LoopDataInputRef.type;
    }

    @Override
    public Class<LoopDataInputRef> getModelType() {
        return LoopDataInputRef.class;
    }

    @Override
    public LoopDataInputRef parseElement(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        LoopDataInputRef loopDataInputRef = new LoopDataInputRef();
        loopDataInputRef.setReference(reader.getElementText());
        return loopDataInputRef;
    }
}

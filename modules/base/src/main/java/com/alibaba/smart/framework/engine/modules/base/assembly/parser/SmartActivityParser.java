package com.alibaba.smart.framework.engine.modules.base.assembly.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartActivity;

/**
 * SmartActivityParser Created by ettear on 16-4-14.
 */
public class SmartActivityParser extends AbstractStAXArtifactParser<SmartActivity> implements StAXArtifactParser<SmartActivity> {

    public SmartActivityParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public SmartActivity parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        SmartActivity smartActivity = new SmartActivity();
        smartActivity.setId(this.getString(reader, "id"));
        smartActivity.setStartActivity(this.getBoolean(reader, "start"));

        this.skipToEndElement(reader);
        return smartActivity;
    }

    @Override
    public void resolve(SmartActivity model, ParseContext context) {
        model.setUnresolved(false);

    }

    @Override
    public QName getArtifactType() {
        return SmartActivity.type;
    }

    @Override
    public Class<SmartActivity> getModelType() {
        return SmartActivity.class;
    }
}

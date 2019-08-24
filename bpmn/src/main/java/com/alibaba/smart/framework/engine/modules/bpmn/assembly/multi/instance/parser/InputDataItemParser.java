package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.InputDataItem;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class InputDataItemParser extends AbstractElementParser<InputDataItem>
      {

    public InputDataItemParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getQname() {
        return InputDataItem.type;
    }

    @Override
    public Class<InputDataItem> getModelType() {
        return InputDataItem.class;
    }

    @Override
    protected InputDataItem parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        InputDataItem inputDataItem = new InputDataItem();
        inputDataItem.setName(XmlParseUtil.getString(reader, "name"));
        return inputDataItem;
    }
}

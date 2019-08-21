package com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  14:55.
 */
public class CallActivityParser  extends AbstractBpmnActivityParser<CallActivity>  {

    public CallActivityParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    //@Override
    //public void resolve(CallActivity model, ParseContext context) throws ResolveException {
    //    model.setUnresolved(false);
    //}

    @Override
    public QName getArtifactType() {
        return CallActivity.type;
    }

    @Override
    public Class<CallActivity> getModelType() {
        return CallActivity.class;
    }

    @Override
    public CallActivity parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        CallActivity callActivity = new CallActivity();
        callActivity.setId(XmlParseUtil.getString(reader, "id"));
        callActivity.setCalledElement(XmlParseUtil.getString(reader, "calledElement"));
        callActivity.setCalledElementVersion(XmlParseUtil.getString(reader, "calledElementVersion"));
        return callActivity;
    }
}

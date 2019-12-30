package com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  14:55.
 */

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = CallActivity.class)

public class CallActivityParser  extends AbstractBpmnParser<CallActivity>  {



    //@Override
    //public void resolve(CallActivity model, ParseContext context) throws ResolveException {
    //    model.setUnresolved(false);
    //}

    @Override
    public QName getQname() {
        return CallActivity.type;
    }

    @Override
    public Class<CallActivity> getModelType() {
        return CallActivity.class;
    }

    @Override
    public CallActivity parseModel(XMLStreamReader reader, ParseContext context) {
        CallActivity callActivity = new CallActivity();
        callActivity.setId(XmlParseUtil.getString(reader, "id"));
        callActivity.setCalledElement(XmlParseUtil.getString(reader, "calledElement"));
        callActivity.setCalledElementVersion(XmlParseUtil.getString(reader, "calledElementVersion"));
        return callActivity;
    }
}

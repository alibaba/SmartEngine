package com.alibaba.smart.framework.engine.bpmn.assembly.callactivity.parser;

import com.alibaba.smart.framework.engine.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import javax.xml.stream.XMLStreamReader;

/** Created by 高海军 帝奇 74394 on 2017 May 14:55. */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = CallActivity.class)
public class CallActivityParser extends AbstractBpmnParser<CallActivity> {

    @Override
    public Class<CallActivity> getModelType() {
        return CallActivity.class;
    }

    @Override
    public CallActivity parseModel(XMLStreamReader reader, ParseContext context) {
        CallActivity callActivity = new CallActivity();
        callActivity.setId(XmlParseUtil.getString(reader, "id"));
        callActivity.setCalledElement(XmlParseUtil.getString(reader, "calledElement"));
        String calledElementVersion = XmlParseUtil.getString(reader, "calledElementVersion");

        if (null == calledElementVersion) {
            calledElementVersion = XmlParseUtil.getString(reader, "calledElementVersionTag");
        }

        callActivity.setCalledElementVersion(calledElementVersion);

        return callActivity;
    }
}

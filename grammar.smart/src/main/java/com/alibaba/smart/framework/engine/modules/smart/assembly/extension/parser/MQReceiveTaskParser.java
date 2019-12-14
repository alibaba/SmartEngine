package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.MQReceiveTask;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = MQReceiveTask.class)

public class MQReceiveTaskParser extends AbstractElementParser<MQReceiveTask> {



    @Override
    protected MQReceiveTask parseModel(XMLStreamReader reader, ParseContext context) {
        MQReceiveTask MQReceiveTask = new MQReceiveTask();
        MQReceiveTask.setName(XmlParseUtil.getString(reader, "name"));
        MQReceiveTask.setGroup(XmlParseUtil.getString(reader, "group"));
        MQReceiveTask.setTopic(XmlParseUtil.getString(reader, "topic"));
        MQReceiveTask.setTag(XmlParseUtil.getString(reader, "tag"));

        return MQReceiveTask;
    }


    @Override
    public QName getQname() {
        return MQReceiveTask.type;
    }

    @Override
    public Class<MQReceiveTask> getModelType() {
        return MQReceiveTask.class;
    }
}

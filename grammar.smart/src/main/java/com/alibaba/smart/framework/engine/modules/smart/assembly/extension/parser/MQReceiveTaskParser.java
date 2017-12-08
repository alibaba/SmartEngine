package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.MQReceiveTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class MQReceiveTaskParser extends AbstractElementParser<MQReceiveTask> {

    public MQReceiveTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected MQReceiveTask parseModel(XMLStreamReader reader, ParseContext context) {
        MQReceiveTask MQReceiveTask = new MQReceiveTask();
        MQReceiveTask.setName(this.getString(reader, "name"));
        MQReceiveTask.setGroup(this.getString(reader, "group"));
        MQReceiveTask.setTopic(this.getString(reader, "topic"));
        MQReceiveTask.setTag(this.getString(reader, "tag"));

        return MQReceiveTask;
    }

    @Override
    protected void parseChild(MQReceiveTask model, BaseElement child) {

    }

    @Override
    public QName getArtifactType() {
        return MQReceiveTask.type;
    }

    @Override
    public Class<MQReceiveTask> getModelType() {
        return MQReceiveTask.class;
    }
}

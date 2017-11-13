package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.MessageQueueReceiveTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class MessageQueueReceiveTaskParser extends AbstractElementParser<MessageQueueReceiveTask> {

    public MessageQueueReceiveTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected MessageQueueReceiveTask parseModel(XMLStreamReader reader, ParseContext context) {
        MessageQueueReceiveTask messageQueueReceiveTask = new MessageQueueReceiveTask();
        messageQueueReceiveTask.setName(this.getString(reader, "name"));
        messageQueueReceiveTask.setConsumerGroup(this.getString(reader, "consumerGroup"));
        messageQueueReceiveTask.setTopic(this.getString(reader, "topic"));
        messageQueueReceiveTask.setTag(this.getString(reader, "tag"));

        return messageQueueReceiveTask;
    }

    @Override
    protected void parseChild(MessageQueueReceiveTask model, BaseElement child) {

    }

    @Override
    public QName getArtifactType() {
        return MessageQueueReceiveTask.type;
    }

    @Override
    public Class<MessageQueueReceiveTask> getModelType() {
        return MessageQueueReceiveTask.class;
    }
}

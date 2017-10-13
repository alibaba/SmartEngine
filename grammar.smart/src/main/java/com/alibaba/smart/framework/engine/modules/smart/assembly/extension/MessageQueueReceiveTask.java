package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTask;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import com.taobao.metaq.client.MetaPushConsumer;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  15:59.
 */
@Data
public class MessageQueueReceiveTask extends AbstractTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageQueueReceiveTask.class);

    public final static QName type = new QName(SmartBase.SMART_NS, "messageQueueReceiveTask");


    private MetaPushConsumer consumer = null;

    private String consumerGroup;

    private String topic;

    private String tag;


}

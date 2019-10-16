package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.AbstractTask;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import com.taobao.metaq.client.MetaPushConsumer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  15:59.
 */
@Data
@EqualsAndHashCode(callSuper = true)

public class MQReceiveTask extends AbstractTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQReceiveTask.class);

    public final static QName type = new QName(SmartBase.SMART_NS, "mqReceiveTask");


    //private MetaPushConsumer consumer = null;

    private String group;

    private String topic;

    private String tag;


}

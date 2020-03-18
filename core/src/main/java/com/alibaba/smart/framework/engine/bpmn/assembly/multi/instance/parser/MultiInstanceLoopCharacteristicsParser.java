package com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance.CompletionCondition;
import com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance.InputDataItem;
import com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:01.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = MultiInstanceLoopCharacteristics.class)

public class MultiInstanceLoopCharacteristicsParser extends AbstractElementParser<MultiInstanceLoopCharacteristics>
{


    @Override
    public QName getQname() {
        return MultiInstanceLoopCharacteristics.type;
    }

    @Override
    public Class<MultiInstanceLoopCharacteristics> getModelType() {
        return MultiInstanceLoopCharacteristics.class;
    }

    @Override
    protected MultiInstanceLoopCharacteristics parseModel(XMLStreamReader reader, ParseContext context) {
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        multiInstanceLoopCharacteristics.setSequential(XmlParseUtil.getBoolean(reader, "isSequential", false));
        return multiInstanceLoopCharacteristics;
    }

    @Override
    protected void decorateChild(MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics, BaseElement child) {
          if (child instanceof InputDataItem) {
            //ignore
        }
         else if (child instanceof CompletionCondition) {
             CompletionCondition condition = (CompletionCondition)child;

            if (CompletionCondition.ACTION_ABORT.equals(condition.getAction())) {
                multiInstanceLoopCharacteristics.setAbortCondition(condition.getExpression());
            } else if (StringUtil.isEmpty(condition.getAction())) {
                // Default
                multiInstanceLoopCharacteristics.setCompletionCondition(condition.getExpression());
            }
        }
        else {
            throw  new EngineException("Should be a instance of CompletionCondition :"+child.getClass());
        }
    }
}
package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionContainer;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class ExecutionListener  implements Extension {
    public final static QName type = new QName(SmartBase.SMART_NS, "executionListener");

    private String[] events;
    private String listener;//fixme 转成Listener这个对象，并且需要将context 提格。

    //FIXME 不必要每次实例化，统一处理机制，运行懒加载，或者托管。
    //private Map<String, List<EventListener>> map = new HashMap<String, List<EventListener>>();

    @Override
    public void decorate(ExtensionContainer extensionContainer) {
        for (String value : events) {
            List<String> list = extensionContainer.getEventListeners().get(value);
            if(null != list){
                list.add(listener);
            }else{
                List<String> strings = new ArrayList<String>();
                strings.add(listener);
                extensionContainer.getEventListeners().put(value,strings);
            }
        }
    }

}

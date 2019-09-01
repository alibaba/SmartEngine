package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.Extensions;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
    public void decorate(Extensions extensions) {
        for (String value : events) {
            List<String> list = extensions.getEventListeners().get(value);
            if(null != list){
                list.add(listener);
            }else{
                List<String> strings = new ArrayList<String>();
                strings.add(listener);
                extensions.getEventListeners().put(value,strings);
            }
        }
    }

}

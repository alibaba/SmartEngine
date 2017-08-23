package com.alibaba.smart.framework.engine.model.assembly;

import java.util.List;

/**
 * @author pengziran
 * Created by pengziran on 01/08/2017.
 */
public interface Element extends BaseElement,Indentity,Invocable{

    /**
     * 获取扩展
     * @return Extentions
     */
    Extensions getExtensions();

    void setExtensions(Extensions extensions);

    /**
     * Get Performers
     * @return Performers
     */
    List<Performable> getPerformers();

    void setPerformers(List<Performable> performables);

}

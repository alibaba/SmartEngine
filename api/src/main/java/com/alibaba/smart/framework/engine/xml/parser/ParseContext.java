package com.alibaba.smart.framework.engine.xml.parser;

import lombok.Data;

/**
 * Created by ettear on 16-4-12.
 */
@Data
public class ParseContext {

    private ParseContext parent;
    private Object currentElement;

    public ParseContext evolve(Object currentElement){
        ParseContext parseContext=new ParseContext();
        parseContext.setParent(this);
        parseContext.setCurrentElement(currentElement);
        return parseContext;
    }
}

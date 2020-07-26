package com.alibaba.smart.framework.engine.extendsion.parser.engine;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import lombok.Data;

/**
 * @author zilong.jiangzl
 * @create 2020-07-16 11:41 下午
 */
@Data
public class StringField implements NoneIdBasedElement {
    static String PROCESS_NS ="http://test.com/process";

    private static final long serialVersionUID = -5129848456612155165L;

    public final static QName qtype = new QName(PROCESS_NS, "string");

    private String value;


}


/*
    static final String ATTRIBUTE_STRING_VALUE = "stringValue";
    static final String ATTRIBUTE_JSON_VALUE = "jsonValue";
    static final String ATTRIBUTE_INTEGER_VALUE = "integerValue";
    static final String ATTRIBUTE_BOOLEAN_VALUE = "booleanValue";
    static final String ATTRIBUTE_LONG_VALUE = "longValue";

 */
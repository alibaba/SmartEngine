//package com.alibaba.smart.framework.engine.pvm.impl;
//
//import com.alibaba.smart.framework.engine.pvm.PvmProcessComponent;
//import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
//import lombok.Data;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author 高海军 帝奇  2016.11.11
// * @author ettear 2016.04.13
// */
//@Data
//public class DefaultRuntimeProcessComponent implements PvmProcessComponent {
//
//    //TODO 和 container 区别
//    private String id;
//    private String version;
//    private PvmProcessDefinition process;
//    private Map<String, PvmProcessDefinition> processes = new HashMap<>();
//
//    @Override
//    public void addProcess(String id, PvmProcessDefinition process) {
//        this.processes.put(id, process);
//    }
//}

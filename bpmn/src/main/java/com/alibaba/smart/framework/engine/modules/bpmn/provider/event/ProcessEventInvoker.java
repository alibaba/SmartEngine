//package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;
//
//import com.alibaba.smart.framework.engine.context.ExecutionContext;
//import com.alibaba.smart.framework.engine.exception.EngineException;
//import com.alibaba.smart.framework.engine.invocation.Invoker;
//import com.alibaba.smart.framework.engine.invocation.message.Message;
//import com.alibaba.smart.framework.engine.invocation.message.impl.SuspendMessage;
//import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvent;
//import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvents;
//import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
//import com.alibaba.smart.framework.engine.modules.bpmn.provider.task.action.SpringAction;
//import com.alibaba.smart.framework.engine.pvm.PvmActivity;
//import com.alibaba.smart.framework.engine.util.ParamChecker;
//
//import java.util.List;
//
///**
// * Created by dongdongzdd on 16/9/20.
// */
//public class ProcessEventInvoker implements Invoker {
//    private PvmActivity pvmActivity;
//
//    public ProcessEventInvoker(PvmActivity pvmActivity) {
//        this.pvmActivity = pvmActivity;
//    }
//
//    @Override
//    public Message invoke(ExecutionContext executionContext) {
//        String assigineEvent = (String) executionContext.getRequest().get("event");
//
//        ServiceTask serviceTask = (ServiceTask) pvmActivity.getModel();
//        ProcessEvents events = serviceTask.getEvents();
//        ParamChecker.notNull(events,"this activity is does not have event !");
//        List<ProcessEvent> eventList =  events.getEvents();
//        if (eventList == null || eventList.isEmpty() || eventList.stream().noneMatch(processEvent -> processEvent.getId().equals(assigineEvent))) {
//           throw new EngineException("this activity is not allow this event !");
//        }
//
//        for (ProcessEvent event:eventList) {
//            if (event.getId().equals(assigineEvent)) {
//                SpringAction springAction = new SpringAction(event.getId(),event.getMethod(),executionContext.getRequest());
//                try {
//                    springAction.buildInstanceRelationShip();
//                } catch (Throwable e) {
//                    //这里好像抓不到异常了已经
//                }
//            }
//        }
//
//        return new SuspendMessage();
//    }
//}

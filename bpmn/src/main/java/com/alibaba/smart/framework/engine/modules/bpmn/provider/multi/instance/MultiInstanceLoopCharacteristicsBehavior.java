package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.provider.ExecutePolicyBehavior;
import com.alibaba.smart.framework.engine.provider.impl.DefaultExecutePolicyBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * @author ettear
 * Created by ettear on 14/10/2017.
 */
public class MultiInstanceLoopCharacteristicsBehavior extends DefaultExecutePolicyBehavior implements ExecutePolicyBehavior {

    private MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics;
    public MultiInstanceLoopCharacteristicsBehavior(
        ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void enter(PvmActivity pvmActivity, ExecutionContext context) {
        //TODO ettear 从UserTaskBehavior把相关逻辑抽取出来
        pvmActivity.invoke(PvmEventConstant.ACTIVITY_START.name(), context);

    }

    @Override
    public void execute(PvmActivity pvmActivity, ExecutionContext context) {
        //TODO ettear 从UserTaskBehavior把相关逻辑抽取出来
        pvmActivity.invoke(PvmEventConstant.ACTIVITY_EXECUTE.name(), context);
    }
}

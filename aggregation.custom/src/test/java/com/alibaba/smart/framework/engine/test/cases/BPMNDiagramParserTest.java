package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  09:52.
 */
public class BPMNDiagramParserTest extends CustomBaseTestCase {


    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("BPMNDiagramParserTest.bpmn.xml");
        assertEquals(5, processDefinition.getProcess().getElements().size());

        //        PersisterSession.destroySession();

    }
}

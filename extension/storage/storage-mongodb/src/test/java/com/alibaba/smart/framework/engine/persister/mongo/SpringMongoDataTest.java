package com.alibaba.smart.framework.engine.persister.mongo;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.mongo.service.MongoActivityInstanceStorage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  20:12.
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional("PlatformTransactionManager")
public class SpringMongoDataTest {

    @Autowired
    private MongoTemplate  mongoTemplate;

    @Autowired
    private MongoActivityInstanceStorage mongoActivityInstanceStorage;

    @Test
    public void test() {

        User user = new User();
        user.setName("Jon");

        mongoTemplate.insert(user, "user");

       User r =  mongoTemplate.findById(user.getId(),User.class);

       Assert.assertNotNull(r);

    }

    @Test
    public void testAI() {

        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();

        TableSchemaStrategy tableSchemaStrategy = new CustomTableSchemaStrategy();
        processEngineConfiguration.setTableSchemaStrategy(tableSchemaStrategy);

        ActivityInstance activityInstance = new DefaultActivityInstance();
        activityInstance.setProcessDefinitionActivityId("a");
        activityInstance.setProcessDefinitionIdAndVersion("version");
        activityInstance.setProcessInstanceId("processInstanceid");
        activityInstance.setStartTime(DateUtil.getCurrentDate());


        mongoActivityInstanceStorage.insert(activityInstance, processEngineConfiguration);


        //Assert.assertNotNull(r);

    }


}
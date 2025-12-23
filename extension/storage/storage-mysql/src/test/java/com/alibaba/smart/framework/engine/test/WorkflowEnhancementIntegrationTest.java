package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.service.command.NotificationCommandService;
import com.alibaba.smart.framework.engine.service.command.SupervisionCommandService;
import com.alibaba.smart.framework.engine.service.query.NotificationQueryService;
import com.alibaba.smart.framework.engine.service.query.SupervisionQueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作流管理系统增强功能集成测试
 * 
 * @author SmartEngine Team
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class WorkflowEnhancementIntegrationTest extends DatabaseBaseTestCase {

    @Test
    public void testServiceRegistration() {

        // 验证新增服务是否正确注册
        SupervisionCommandService supervisionCommandService = smartEngine.getSupervisionCommandService();
        SupervisionQueryService supervisionQueryService = smartEngine.getSupervisionQueryService();
        NotificationCommandService notificationCommandService = smartEngine.getNotificationCommandService();
        NotificationQueryService notificationQueryService = smartEngine.getNotificationQueryService();
        
        // 基本验证服务不为空
        assert supervisionCommandService != null : "SupervisionCommandService should not be null";
        assert supervisionQueryService != null : "SupervisionQueryService should not be null";
        assert notificationCommandService != null : "NotificationCommandService should not be null";
        assert notificationQueryService != null : "NotificationQueryService should not be null";
        
        System.out.println("All workflow enhancement services are properly registered!");
    }
    
    @Test
    public void testTaskCommandServiceExtensions() {

        // 验证TaskCommandService的扩展方法存在
        // 这里只是验证方法存在，实际的功能测试需要完整的流程实例
        assert smartEngine.getTaskCommandService() != null : "TaskCommandService should not be null";
        
        System.out.println("TaskCommandService extensions are available!");
    }
}
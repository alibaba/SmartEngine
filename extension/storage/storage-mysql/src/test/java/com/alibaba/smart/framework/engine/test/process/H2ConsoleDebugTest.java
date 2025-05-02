//package com.alibaba.smart.framework.engine.test.process;
//
//import org.h2.tools.Server;
//import org.junit.jupiter.api.*;
//
//import java.sql.SQLException;
//
//public class H2ConsoleDebugTest {
//
//    static Server webServer;
//
//    @BeforeAll
//    public static void startH2Console() throws SQLException {
//        webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
//        System.out.println("H2 Console started at http://localhost:8082");
//    }
//
//    @AfterAll
//    public static void stopH2Console() {
//        if (webServer != null) webServer.stop();
//    }
//
//    @Test
//    public void testWithConsole() throws InterruptedException {
//        System.out.println("Keep the test running to inspect H2 console...");
//        Thread.sleep(600000); // 保持 JVM 活着，用于手动调试
//    }
//}

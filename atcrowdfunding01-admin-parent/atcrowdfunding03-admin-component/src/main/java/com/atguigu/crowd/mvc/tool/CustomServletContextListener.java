package com.atguigu.crowd.mvc.tool;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class CustomServletContextListener  implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent var1) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent var1) {
        // 解决Tomcat mysql 驱动内存泄漏，手动注销JDBC
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while (drivers.hasMoreElements()) {
            try {
                d = drivers.nextElement();
                DriverManager.deregisterDriver(d);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        AbandonedConnectionCleanupThread.checkedShutdown();
    }
}
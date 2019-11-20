package com.java.www;

import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 测试JDBC
 */

public class JdbcTest {
    // 方法

    @Test
    public void testDriver() throws SQLException {
        // 1. 创建一个Driver实现类的对象
        Driver driver = new com.mysql.jdbc.Driver();
        /*
        * mysql-connector-java-8 建议使用 com.mysql.cj.jdbc.Driver 驱动类
        * */

        // 2. 提供连接数据库的的基本信息：url, user, password,
        String url = "jdbc:mysql://127.0.0.1:3306/testdb?serverTimezone=Asia/Shanghai";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "py123456");

        // 3. 调用 Driver 接口的 connect(url, info) 方法
        Connection connection = driver.connect(url, info);
        System.out.println(connection);
        /*
         * Loading class `com.mysql.jdbc.Driver'.This is deprecated.
         * The new driver class is `com.mysql.cj.jdbc.Driver'.
         * The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
         * */
    }


    /**
     * 通过Driver实现类获取数据库的通用方法
     *
     * jdbc.properties配置文件放在src目录下
     *
     * @return 数据库连接对象
     * @throws Exception
     */
    public Connection getConnection() throws Exception {
        // 读取jdbc.properties文件配置
        InputStream is = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);

        // 通过反射加载并注册数据库驱动
        String driverClass = properties.getProperty("driverClass");
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        Class clazz = Class.forName(driverClass);
        Driver driver = (Driver) clazz.newInstance();
        Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);

        // 通过Driver 的 connect 方法获取数据库连接对象
        Connection connection = driver.connect(jdbcUrl, info);
        return connection;
    }

    @Test
    public void test1() throws Exception {
        System.out.println(getConnection());
    }
}

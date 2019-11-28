package com.java.www;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 操作JDBC的工具类
 *
 * 需要在src根路径下，创建jdbc.properties文件，并提供连接数据库基本信息
 * driverClass =
 * jdbcUrl =
 * user =
 * password =
 */
public class JdbcUtils {
    /**
     * 获取数据库连接对象
     *
     * @return
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 读取jdbc.properties文件配置
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(in);

            String driverClass = properties.getProperty("driverClass");
            String jdbcUrl = properties.getProperty("jdbcUrl");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            // 加载数据库驱动并注册
            Class.forName(driverClass);

            // 通过 DriverManager类的getConnection 获取数据库连接对象，并返回
            connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (IOException e) {
            System.out.println("读取配置文件异常");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("无此数据库驱动类");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("连接数据库出现异常");
        }
        return connection;
    }

    /**
     * 释放JDCB资源
     *
     * @param resultSet: ResultSet对象
     * @param statement: Statement对象
     * @param connection: 数据库连接对象
     */
    public static void release(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void release(Statement statement, Connection connection) {
        release(null, statement, connection);
    }

    public static void release(Connection connection) {
        release(null, null, connection);
    }
}

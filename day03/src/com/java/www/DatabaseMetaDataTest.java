package com.java.www;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DatabaseMetaData 类
 * 是描述 数据库 的元数据对象
 * 可以由 Connection 得到DatabaseMetaData对象
 *
 * ## 方法
 * getURL()：返回一个String类对象，代表数据库的URL
 * getUserName()：返回连接当前数据库管理系统的用户名
 * isReadOnly()：返回一个boolean值，指示数据库是否只允许读操作
 * getDatabaseProductName()：返回数据库的产品名称
 * getDatabaseProductVersion()：返回数据库的版本号
 * getDriverName()：返回驱动驱动程序的名称
 * getDriverVersion()：返回驱动程序的版本号
 */
public class DatabaseMetaDataTest {
    @Test
    public void test() {
        Connection conn = null;
        ResultSet resultSet = null;

        try {
            conn = JdbcUtils.getConnection();
            DatabaseMetaData databaseMetaData = conn.getMetaData();

            // 获取数据库的版本
            int version = databaseMetaData.getDatabaseMajorVersion();
            System.out.println("version: " + version);

            // 获取当前会话连接数据库的用户名
            String user = databaseMetaData.getUserName();
            System.out.println("username: " + user);

            // getURL()
            String url = databaseMetaData.getURL();
            System.out.println("url: " + url);

            // getDatabaseProductVersion()
            String databaseProductVersion =  databaseMetaData.getDatabaseProductVersion();
            System.out.println("databaseProductVersion: " + databaseProductVersion);

            // getDriverName()
            String driverName = databaseMetaData.getDriverName();
            System.out.println("driverName: " + driverName);

            // isReadOnly()
            boolean b = databaseMetaData.isReadOnly();
            System.out.println("是否值只允许只读：" + b);

            // 获取数据库列表集
            resultSet = databaseMetaData.getCatalogs();
            System.out.println("数据库列表：");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, conn);
        }
    }
}

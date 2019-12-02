package com.java.www;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * c3p0 连接池工具
 *
 *  需要在src根路径下，创建 c3p0-config.xml或c3p0.properties文件，并提供连接数据库基本信息
 */
public class C3p0Utils {
    private static DataSource dataSource = null;

    static {
//        dataSource = new ComboPooledDataSource(); // default-config配置方式
        dataSource = new ComboPooledDataSource("intergalactoApp"); // named-config配置方式，传入named-config名
    }

    // 构造器
    public C3p0Utils() {}

    // 方法
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
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
                // 在连接池中，此操作并非真正关闭连接，而是把连接对象归还连接池
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

    public static void release(ResultSet resultSet, Connection connection) {
        release(resultSet, null, connection );
    }

    /**
     * 开启事务
     * @param connection: Connection对象
     */
    public static void beginTransaction(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     * @param connection: Connection对象
     */
    public static void commitTransaction(Connection connection) {
        if (connection == null) return;
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 回滚事务
     * @param connection: Connection对象
     */
    public static void rollbackTransaction(Connection connection) {
        if (connection == null) return;
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

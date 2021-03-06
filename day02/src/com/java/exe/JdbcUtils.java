package com.java.exe;

import com.mysql.cj.jdbc.ClientPreparedStatement;

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
            InputStream is = JdbcUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(is);
            is.close();

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
            System.out.println("无此数据库驱动类");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("连接数据库出现异常");
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

    /**
     * 通用的Statement方式SQL更新方法
     * 包括：insert, update, delete，建表，建库等
     *
     * @param sql: sq语句
     * @return : 执行sql语句手影响的行数
     */
    public static int update(String sql) {
        int rows = 0; // 执行sql语句手影响的行数
        Connection conn = getConnection();
        Statement statement = null;

        try {
            statement = conn.createStatement();
            rows = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(statement, conn);
        }
        return rows;
    }

    /**
     * 通用的PreparedStatement方式更新sql语句方法
     *
     * @param sql: sql语句
     * @param args: sql语句中占位符对应的可变参数，顺序需要与?占位符顺序一致
     * @return: 执行sql语句影响的行数
     */
    public static int update(String sql, Object... args) {
        int rows = 0;
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = JdbcUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);

            // 设置占位符值
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            // 获取 占位符SQL语句替换后的SQL语句
            ClientPreparedStatement cps = (ClientPreparedStatement) preparedStatement;
            System.out.println("sql语句:\n" + cps.asSql());
            // 执行sql语句
            rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(preparedStatement, conn);
        }
        return rows;
    }

    /**
     * 通用的查询方法：可以根据传入的Class 对象、SQL、返回 SQL 对应的记录的对象
     *
     * @param clazz: 要返回的对象类型
     * @param sql: sql语句
     * @param args: sql语句的?占位对应的参数
     * @param <T>: 泛型类型
     * @return: 一个泛型类型T的对象
     */
    public static <T> T get(Class<T> clazz, String sql, Object... args) {
        T entity = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 建立连接
            conn = JdbcUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            // 2. 给sql占位符设置值
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            System.out.println("替换参数后的sql:");
            System.out.println(((ClientPreparedStatement) preparedStatement).asSql());
            // 3. 获得sql执行结果集
            resultSet = preparedStatement.executeQuery();

            // 4. 得到ResultSetMetaData对象
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 5. 获取结果集第一行，遍历列值，并给新建的clazz对象的field值
            if (resultSet.next()) {
                entity = clazz.newInstance();
                for (int i = 0; i < resultSetMetaData.getColumnCount(); ++i) {
                    String columnLable = resultSetMetaData.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);
                    // 创建clazz对应类的实例
                    // 给上面的实例entity设置field值
                    ReflectionUtils.setFieldValue(entity, columnLable, columnValue);
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }

        return entity;
    }

}


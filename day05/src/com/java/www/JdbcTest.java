package com.java.www;

import org.junit.Test;

import java.sql.*;

/**
 * 使用JDBC 调用数据库中的存储过程、函数
 *
 * CallableStatement接口方法
 * registerOutParameter(int parameterIndex, int sqlType) 设置OUT类型参数值
 * setXxx() 设置IN、OUT、INOUT参数值，setObject方法更简单
 * null值可以用callableStatement.setNull(int parameterIndex, int sqlType);
 *
 * JDBC调用的存储过程的SQL语法：{call 存储过程名(参数...)};
 */
public class JdbcTest {
    /**
     * JDBC调用存储过程
     *
     * 测试输入用户名、密码，返回登录是否成功
     * PROCEDURE login_procdr(IN username VARCHAR(32), IN pwd VARCHAR(32), OUT valid INT)
     */
    @Test
    public void testCallableStatment() {
        Connection conn = null;
        CallableStatement callableStatement = null;
        try {
            conn = DbcpUtils.getConnection();
            String sql = "{call login_procdr(?, ?, ?)}";
//            String sql = "{call login_procdr(?, ?, ?);}"; // 错误写法，分号不能写在{ }内，可以写在{ }外
//            String sql = "{call login_procdr(?, ?, ?)};"; // 正确写法
            callableStatement = conn.prepareCall(sql);
            callableStatement.setObject(1, "马云");
            callableStatement.setObject(2, "my123");
            callableStatement.registerOutParameter(3, Types.INTEGER); // 也可以用callableStatement.setObject(3, Types.INTEGER);

            // 执行存储过程
            boolean b = callableStatement.execute();

            // 获取有返回值的存储过程
            int isValid = callableStatement.getInt(3);
            System.out.println("isValid: " + isValid);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpUtils.release(callableStatement, conn);
        }
    }


    /**
     * JDBC调用函数
     * FUNCTION login_func(username VARCHAR(32), pwd VARCHAR(32)) RETURNS INT
     * 与一般的查询一样
     */
    @Test
    public void testFunction() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = DbcpUtils.getConnection();
            String sql = "select login_func(?, ?);";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1, "马云");
            preparedStatement.setObject(2, "my123");

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int valid = resultSet.getInt(1);
                System.out.println(valid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpUtils.release(resultSet, preparedStatement, conn);
        }
    }
}

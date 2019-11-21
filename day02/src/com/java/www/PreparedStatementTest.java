package com.java.www;

import com.mysql.cj.ClientPreparedQuery;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementTest {

    /**
     * preparedStatement.setXxx(int parameterIndex, Xxx x)
     * parameterIndex: sql语句中?参数占位符的索引，从1开始
     * preparedStatement.setObject(int parameterIndex, Object o)
     *
     * @param employee
     * @return
     */
    public static int addNewEmployee(Employee employee) {
        int rows = 0;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        // String sql = "INSERT INTO employees (`name`, age) VALUES ('%s', %s);";
        String sql = "INSERT INTO employees (`name`, age) VALUES (?, ?);";
        try {
            conn = JdbcUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, employee.getName()); // 设置第1个占位符的值
            preparedStatement.setInt(2, employee.getAge()); // // 设置第2个占位符的值

            // 获取 占位符SQL语句替换后的SQL语句
            ClientPreparedStatement cps = (ClientPreparedStatement) preparedStatement;
            System.out.println(cps.asSql());
            rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(preparedStatement, conn);
        }
        return rows;
    }

    @Test
    public void testAddNewEmployee() {
        Employee e = new Employee("Suzen", 24);
        int rows = addNewEmployee(e);
        System.out.println(rows);
    }

    @Test
    public void test2() {
        String sql = "INSERT INTO employees VALUES (null, ?, ?, ?);";
        String name = "a' OR passwd = ";
        int age = 26;
        String passwd = " OR '1' = '1";

        JdbcUtils.update(sql, name, age, passwd);
    }
}

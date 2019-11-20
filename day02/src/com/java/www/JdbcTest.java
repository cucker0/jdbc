package com.java.www;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTest {

    public void addNewEmployee(Employee employee) {
        String sql = String.format("INSERT INTO employees (`name`, age) VALUES ('%s', %s);", employee.getName(), employee.getAge());
        System.out.println("sql语句: " + sql);
        JdbcUtils.update(sql);
    }

    @Test
    public void testAddNewEmployee() {
        Employee e = new Employee("katy", 24);
        addNewEmployee(e);
    }

    /**
     * 拼接SQL方式的 SQL注入
     *
     * name: 用户名
     * passwd: 密码
     * 这两个变量有用户输入，用name和passwd查询是否有有记录，有则登录成功，否则失败
     */
    @Test
    public void testSQLInjection() {
//        String name = "katy";
//        String passwd = "katy22";
        String name = "a' OR passwd = ";
        String passwd = " OR '1' = '1";

        String sql = String.format("SELECT * FROM employees WHERE `name` = '%s' AND passwd = '%s';", name, passwd);
        System.out.println("sql语句: \n" + sql);

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = JdbcUtils.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                System.out.println("登录成功");
            } else {
                System.out.println("用户名、密码不匹配或用户不存在");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, statement, conn);
        }
    }
}

package com.java.www;

import com.mysql.cj.jdbc.ClientPreparedStatement;
import org.junit.Test;

import java.sql.*;

public class JdbcTest {

    /**
     * 把给定的Employee对象信息插入数据库
     *
     * @param employee
     */
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
     * 主要目的改变原SQL筛选条件，加一个恒成立的条件
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
        // 拼接后sql语句：sql ==> "SELECT * FROM employees WHERE `name` = 'a' OR passwd = ' AND passwd = ' OR '1' = '1';"

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

    /**
     * 使用PreparedStatement可以有效防止sql注入
     */
    @Test
    public void testSQLInjection2() {
        String name = "a' OR passwd = ";
        String passwd = " OR '1' = '1";
        String sql = "SELECT * FROM employees WHERE `name` = ? AND passwd = ?;"; // 替换后的语句为：SELECT * FROM employees WHERE `name` = 'a'' OR passwd = ' AND passwd = ' OR ''1'' = ''1';

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = JdbcUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, passwd);

            ClientPreparedStatement cps = (ClientPreparedStatement) preparedStatement;
            System.out.println("sql语句:\n" + cps.asSql());

//            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("登录成功");
            } else {
                System.out.println("用户名、密码不匹配或用户不存在");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }
    }



}

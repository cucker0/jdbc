package com.java.exe;

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

            resultSet = preparedStatement.executeQuery();

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

    public Employee getEmployee(String sql, Object... args) {
        Employee employee = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = JdbcUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                employee = new Employee();
                employee.setId(resultSet.getInt(1));
                employee.setName(resultSet.getString(2));
                employee.setAge(resultSet.getInt(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }
        return employee;
    }

    @Test
    public void testGetEmployee() {
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees WHERE id = ?;";
        Employee e = getEmployee(sql, 7);
        System.out.println(e);
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
    public <T> T get(Class<T> clazz, String sql, Object... args) {
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
            // 3. 获得sql执行结果集
            resultSet = preparedStatement.executeQuery();

            // 4. 得到ResultSetMetaData对象
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 5. 获取结果集第一行，遍历列值，并给新建的clazz对象的field值
            if (resultSet.next()) {
                for (int i = 0; i < resultSetMetaData.getColumnCount(); ++i) {
                    String columnLable = resultSetMetaData.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);
                    // 创建clazz对应类的实例
                    entity = clazz.newInstance();
                    // 给上面的实例entity设置field值
                    ReflectionUtils.setFieldValue(entity, columnLable, columnValue);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }

        return entity;
    }

    @Test
    public void testGet() {
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees WHERE id = ?;";
        Class<Employee> clazz = Employee.class;
        Employee e = get(clazz, sql, 7);
        System.out.println(e);
    }
}

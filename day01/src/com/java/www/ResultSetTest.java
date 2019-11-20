package com.java.www;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetTest {
    /**
     * ResultSet
     *
     * 查询数据
     * 1. 调用 Statement 对象的 executeQuery(sql) 可以得到结果集.
     * 2. ResultSet 返回的实际上就是一张数据表. 有一个指针指向数据表的第一样的前面.
     * 可以调用 next() 方法检测下一行是否有效. 若有效该方法返回 true, 且指针下移. 相当于
     * Iterator 对象的 hasNext() 和 next() 方法的结合体
     * 3. 当指针对位到一行时, 可以通过调用 getXxx(index) 或 getXxx(columnName)
     * 获取每一列的值. 例如: getInt(1), getString("name")
     * 4. ResultSet 当然也需要进行关闭.
     */
    @Test
    public void testResultSet() {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            // 1. 获取数据库连接、statement
            conn = JdbcUtils.getConnection();
            statement = conn.createStatement();

            // 2. 提供查询SQL语句
            String sql = "SELECT id, name, age FROM employees;";

            // 3. 执行SQL语句
            rs = statement.executeQuery(sql);
            System.out.println(rs);

            // 4. 遍历结果集
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString("name");
                int age = rs.getInt(3);
                System.out.printf("id:%s, name:%s, age:%s\n", id, name, age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(rs, statement, conn);
        }
    }

    @Test
    public void testResultSet2() {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            // 1. 获取数据库连接、statement
            conn = JdbcUtils.getConnection();
            statement = conn.createStatement();

            // 2. 提供查询SQL语句
            String sql = "select * from \"DnsRecord_record\";";

            // 3. 执行SQL语句
            rs = statement.executeQuery(sql);
            System.out.println(rs);

            // 4. 遍历结果集
            while (rs.next()) {
                int id = rs.getInt(1);
                String f2 = rs.getString(2);
                String f3 = rs.getString(3);
                System.out.printf("id:%s, zone:%s, host:%s\n", id, f2, f3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(rs, statement, conn);
        }
    }
}

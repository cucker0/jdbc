package com.java.www;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * JDBC中使用事务
 *
 */
public class TransactionTest {
    /**
     * account表，张无忌账号转账100到赵敏账号
     *
     */
    @Test
    public void testNonTransaction() {
        Connection conn = null;
        conn = JdbcUtils.getConnection();

        DAO dao = new DAO();
        String sql = "UPDATE account SET balance = balance - 100 WHERE `name` = '张无忌';";
        String sql2 = "UPDATE account SET balance = balance + 100 WHERE `name` = '赵敏';";
        dao.update(sql);
        int i = 10 / 0;
        System.out.println(i);

        dao.update(sql2);
    }

    /**
     * account表，张无忌账号转账100到赵敏账号
     * 利用事务，成功了，提交事务，失败了回滚事务
     *
     *
     * 为方便测试，把两账号balance都设置为2000，再测试
     */
    @Test
    public void testTransaction() {
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            System.out.println("自动提交事务是开启: " + conn.getAutoCommit());
            // 开启事务: 要求关闭事务自动提交
            conn.setAutoCommit(false);
            String sql = "UPDATE account SET balance = balance - 100 WHERE `name` = '张无忌';";
            String sql2 = "UPDATE account SET balance = balance + 100 WHERE `name` = '赵敏';";
            update(conn, sql);
            int i = 10 / 0;
            System.out.println(i);

            update(conn, sql2);
            // 提交事务
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            // 发生异常，回滚事务
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // 恢复事务自动提交
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JdbcUtils.release(conn);
        }
    }

    /**
     * 使用事务的SQL更新操作(INSERT、UPDATE、DELETE)
     * 用指定的Connection对象、执行指定的更新sql语句
     *
     * @param connection: 数据库Connection对象
     * @param sql: 更新语句,可带?占位符
     * @param args: ?占位符对应的参数
     * @return: 执行sql受影响的行数
     */
    public int update(Connection connection, String sql, Object... args) {
        int rows = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[0]);
            }
            rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(preparedStatement, null);
        }
        return rows;
    }


    /**
     * 测试事务隔离级别
     */
    @Test
    public void testTransactionIsolationUpdate() {
        Connection conn = JdbcUtils.getConnection();
        conn = JdbcUtils.getConnection();
        try {
            System.out.println("自动提交事务是开启: " + conn.getAutoCommit());
            // 开始事务: 要求关闭事务自动提交
            conn.setAutoCommit(false);
            String sql = "UPDATE account SET balance = balance - 100 WHERE `name` = '张无忌';";
            String sql2 = "UPDATE account SET balance = balance + 100 WHERE `name` = '赵敏';";
            update(conn, sql);
            update(conn, sql2); // 在此，设置断点调试
            // 提交事务
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JdbcUtils.release(conn);
        }
    }

    /**
     * 测试事务隔离级别
     */
    @Test
    public void testTransactionIsolationRead() {
        String sql = "SELECT balance FROM account WHERE `name` = '张无忌';";
        double balance = getForValue(sql);
        System.out.println(balance);
    }

    public <E> E getForValue(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = JdbcUtils.getConnection();
            // 设置指定连接的事务隔离级别
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            preparedStatement = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            System.out.println("事务隔离级别：" + conn.getTransactionIsolation());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }
        return null;
    }
}

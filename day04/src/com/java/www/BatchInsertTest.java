package com.java.www;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 批量插入
 */
public class BatchInsertTest {
    @Test
    public void testBatchWithStatement1() {
        Connection conn = null;
        Statement statement = null;
        String sql = null;
        try {
            conn = JdbcUtils.getConnection();
            // 开启事务
            JdbcUtils.beginTransaction(conn);
            statement = conn.createStatement();

            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; ++i) {
                sql = String.format("INSERT INTO employees (`name`, age) VALUES ('%s', %s);", "user" + i, 18);
                statement.executeUpdate(sql);
            }
            long end = System.currentTimeMillis();

            System.out.printf("用时：%sms\n", (end - start)); // 13083ms
            // 提交事务
            JdbcUtils.commitTransaction(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            // 回滚事务
            JdbcUtils.rollbackTransaction(conn);
        } finally {
            JdbcUtils.release(statement, conn);
        }
    }

    /**
     * Statement
     * statement.addBatch(sql)
     */
    @Test
    public void testBatchWithStatement2() {
        Connection conn = null;
        Statement statement = null;
        String sql = null;
        try {
            conn = JdbcUtils.getConnection();
            // 开启事务
            JdbcUtils.beginTransaction(conn);
            statement = conn.createStatement();

            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; ++i) {
                sql = String.format("INSERT INTO employees (`name`, age) VALUES ('%s', %s);", "user" + i, 18);
                statement.addBatch(sql);
            }
            statement.executeBatch();
            long end = System.currentTimeMillis();

            System.out.printf("用时：%sms\n", (end - start)); // 15148ms
            // 提交事务
            JdbcUtils.commitTransaction(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            // 回滚事务
            JdbcUtils.rollbackTransaction(conn);
        } finally {
            JdbcUtils.release(statement, conn);
        }
    }

    /**
     * PreparedStatement
     */
    @Test
    public void testBatchWithPreparedStatement1() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = JdbcUtils.getConnection();
            JdbcUtils.beginTransaction(conn);
            String sql = "INSERT INTO employees (`name`, age) VALUES (?, ?);";
            preparedStatement = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; ++i) {
                preparedStatement.setObject(1, "acc" + i);
                preparedStatement.setObject(2, 18);
                preparedStatement.executeUpdate();
            }
            long end = System.currentTimeMillis();
            System.out.printf("用时：%sms\n", (end - start)); // 12919ms
            JdbcUtils.commitTransaction(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(preparedStatement, conn);
        }
    }

    /**
     * 该方法效率是这几种方法中最高的
     *
     * PreparedStatement
     * preparedStatement.addBatch()
     *
     */
    @Test
    public void testBatchWithPreparedStatement2() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = JdbcUtils.getConnection();
            JdbcUtils.beginTransaction(conn);
            String sql = "INSERT INTO employees (`name`, age) VALUES (?, ?);";
            preparedStatement = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; ++i) {
                preparedStatement.setObject(1, "acc" + i);
                preparedStatement.setObject(2, 18);
                // "积攒"sql
                preparedStatement.addBatch();
                // 当"积攒"到一定量的时候，统一执行一次，并且清空先前"积攒"的sql。如没300条执行一次
                if ((i + 1) % 1024 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }

            // 总条数不是批量数值的整数倍，还需要额外执行一次
            if (100000 % 1024 != 0) {
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }
            long end = System.currentTimeMillis();
            System.out.printf("用时：%sms\n", (end - start)); // 12143ms
            JdbcUtils.commitTransaction(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(preparedStatement, conn);
        }
    }
}

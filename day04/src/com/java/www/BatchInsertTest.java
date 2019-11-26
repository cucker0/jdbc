package com.java.www;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 批量插入
 */
public class BatchInsertTest {
    @Test
    public void testBatchWithStatement() {
        Connection conn = null;
        Statement statement = null;
        String sql = null;
        try {
            conn = JdbcUtils.getConnection();
            // 开启事物
            JdbcUtils.beginTransaction(conn);
            statement = conn.createStatement();

            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; ++i) {
                sql = String.format("INSERT INTO employees (`name`, age) VALUES ('%s', %s);", "user" + 18);
                statement.addBatch(sql);
            }
            long end = System.currentTimeMillis();

            System.out.printf("用时：%ss\n", (end - start) / 1000);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(statement, conn);
        }
    }
}

package com.java.www;

import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

/**
 * 使用JDBC 调用数据库中的存储过程、函数
 *
 */
public class JdbcTest {
    /**
     *
     *
     */
    @Test
    public void testCallableStatment() {
        Connection conn = null;
        CallableStatement callableStatement = null;
        conn = DbcpUtils.getConnection();
        String sql = "{? = CALL login_is_valid('马云', 'my123');}";
        callableStatement = conn.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.NUMERIC);
        callableStatement.setObject(2, "马云");
        callableStatement.setObject(3, "my123");
    }

}

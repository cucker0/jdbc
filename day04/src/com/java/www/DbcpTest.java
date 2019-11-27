package com.java.www;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

public class DbcpTest {
    @Test
    public void testDbcp() {
        final BasicDataSource dataSource = new BasicDataSource();

        // 为数据源示例设置基本信息
        dataSource.setUsername("root");
        dataSource.setPassword("py123456");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 连接池连接个数
        dataSource.setInitialSize(5);

    }
}

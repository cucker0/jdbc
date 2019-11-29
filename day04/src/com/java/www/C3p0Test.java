package com.java.www;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * c3p0连接池
 *
 * 依赖jar包：c3p0.jar、commons-logging.jar
 */
public class C3p0Test {
    @Test
    public void testC3p0() {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass("com.mysql.cj.jdbc.Driver"); // loads the jdbc driver
            cpds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false");
            cpds.setUser("root");
            cpds.setPassword("py123456");

            Connection conn = cpds.getConnection();
            System.out.println(conn);

            cpds.close();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testC3p0WithConfigFile() throws Exception {
//        DataSource dataSource = new ComboPooledDataSource(); // default-config配置方式
         DataSource dataSource = new ComboPooledDataSource("intergalactoApp"); // named-config配置方式，传入named-config名
        System.out.println(dataSource.getConnection());

        ComboPooledDataSource comboPooledDataSource = (ComboPooledDataSource) dataSource;
        System.out.println(comboPooledDataSource);
        System.out.println(comboPooledDataSource.getMaxStatements());
        System.out.println(comboPooledDataSource.getConnectionPoolDataSource());
        Connection conn = comboPooledDataSource.getConnection();
        System.out.println(conn);
    }

    @Test
    public void testC3p0Utils() {
        Connection conn = C3p0Utils.getConnection();
        System.out.println(conn);
    }
}

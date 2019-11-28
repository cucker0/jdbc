package com.java.www;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.ref.PhantomReference;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * dbcp连接池
 * jar包依赖：commons-dbcp.jar、commons-pool.jar、commons-logging.jar
 *
 */
public class DbcpTest {
    @Test
    public void testDbcp() {
        // 1. 创建BasicDataSource对象
        final BasicDataSource dataSource = new BasicDataSource();

        // 2. 为数据源示例设置基本信息
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("py123456");
        /*
        * 3. 指定数据源的可选属性
        * */
        // 指定数据库连接池中初始化连接数的个数
        dataSource.setInitialSize(5);
        // 指定最大的活动连接数，同一时刻可以同时向数据库申请的连接数
        dataSource.setMaxTotal(5);
        // 指定最小空闲连接数，在数据库连接池中保存的最少的空闲连接的数量
        dataSource.setMinIdle(2);
        // 等待数据库连接池分配连接的最长时间. 单位为毫秒. 超出该时间将抛出异常.
        dataSource.setMaxWaitMillis(1000 * 5);

        // 4. 从数据源中获取数据库连接对象
        try {
            // 先创建5个连接
            Connection conn = dataSource.getConnection();
            System.out.println(conn.getClass());

            for (int i = 0; i < 3; ++i) {
                conn = dataSource.getConnection();
                System.out.println(conn.getClass());
            }
            Connection conn2 = dataSource.getConnection();
            System.out.println("conn2: " + conn2.getClass());

            // 另外启动一个线程来创建一个连接
            new Thread() {
                @Override
                public void run() {
                    try {
                        Connection conn3 = dataSource.getConnection();
                        System.out.println("conn3: " + conn3.getClass());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

//            Thread.sleep(4500); // setMaxWaitMillis未超时前，关闭conn2后，conn3也能正常创建
            Thread.sleep(5500); // 超过setMaxWaitMillis时间后，conn3因连接等待超时，未因创建连接
            conn2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDbcpWithDataSourceFactory() {
        Properties properties = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("dbcp.properties");
        try {
            properties.load(is);
            DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
            Connection conn = dataSource.getConnection();
            System.out.println(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDbcpUtils() {
        Connection connection = DbcpUtils.getConnection();
        System.out.println(connection);
    }
}

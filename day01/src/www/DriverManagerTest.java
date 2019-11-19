package www;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DriverManagerTest {
    /**
     * DriverManager 驱动管理类
     *
     *  可以通过getConnection()方法获取数据库连接对象
     *  可以同时管理多个不同类型的数据库驱动，通过getConnection输入的参数不同来区分多个不同类型的数据库
     */
    @Test
    public void testDriverManager() throws Exception {
        // 1. 提供连接数据库的基本信息
        String driverClass = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";
        String user = "root";
        String password = "py123456";

        // 2. 加载数据库驱动(并自动注册驱动，com.mysql.cj.jdbc.Driver类有静态代码块 DriverManager.registerDriver(new Driver());)
        Class.forName(driverClass);

        // 3. 通过DriverManager 的 getConnection 方法获取数据库连接对象
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        System.out.println("DriverManager");
        System.out.println(connection);
    }

    /**
     * 通过 DriverManager 实现类获取数据库连接对象
     *
     * @return 数据库连接对象
     * @throws Exception
     */
    public Connection getConnection() throws Exception {
        // 读取jdbc.properties文件配置
        InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(in);

        String driverClass = properties.getProperty("driver");
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        // 加载数据库驱动并注册
        Class.forName(driverClass);

        // 通过 DriverManager类的getConnection 获取数据库连接对象，并返回
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        return connection;
    }

    @Test
    public void test2() throws Exception {
        System.out.println(getConnection());
    }
}

package com.java.www;

import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 测试JDBC
 */

public class JdbcTest {
    // 方法

    @Test
    public void testDriver() throws SQLException {
        // 1. 创建一个Driver实现类的对象
        Driver driver = new com.mysql.jdbc.Driver();
        /*
        * mysql-connector-java-8 建议使用 com.mysql.cj.jdbc.Driver 驱动类
        * */

        // 2. 提供连接数据库的的基本信息：url, user, password,
        String url = "jdbc:mysql://127.0.0.1:3306/testdb?serverTimezone=Asia/Shanghai";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "py123456");

        // 3. 调用 Driver 接口的 connect(url, info) 方法
        Connection connection = driver.connect(url, info);
        System.out.println(connection);
        /*
         * Loading class `com.mysql.jdbc.Driver'.This is deprecated.
         * The new driver class is `com.mysql.cj.jdbc.Driver'.
         * The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
         * */
    }


    /**
     * 通过Driver实现类获取数据库的通用方法
     *
     * jdbc.properties配置文件放在src目录下
     *
     * @return 数据库连接对象
     * @throws Exception
     */
    public Connection getConnection() throws Exception {
        // 读取jdbc.properties文件配置
        InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(in);

        // 通过反射加载并注册数据库驱动
        String driverClass = properties.getProperty("driver");
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        Driver driver = (Driver) Class.forName(driverClass).newInstance();
        Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);

        // 通过Driver 的 connect 方法获取数据库连接对象
        Connection connection = driver.connect(jdbcUrl, info);
        return connection;
    }

    @Test
    public void test1() throws Exception {
        System.out.println(getConnection());
    }

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
    public Connection getConnection2() throws Exception {
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
        System.out.println(getConnection2());
    }

    /**
     * Statement
     *
     * 该对象的executeUpdate(String sql) 只允许是更新的sql语句，即,insert, update, delete表数据操作
     * sql语句为select报异常
     */
    @Test
    public void testStatement() {
        // 1. 获取数据库连接
        Connection conn = null;
        Statement statement = null;

        try {
            conn = getConnection2();
            statement = conn.createStatement();

            // 2. 准备插入语句
//            String sql = "INSERT INTO employees (`name`, age) VALUES ('Lily', 23);";
//            String sql = "INSERT INTO employees (`name`, age) VALUES ('Marry', 28);";
//            String sql = "INSERT INTO employees (`name`, age) VALUES ('Denny', 26);";
//            String sql = "UPDATE employees SET age = 18 WHERE `name` = 'Denny';";
            String sql = "SELECT * FROM employees;"; // 报异常：SQLException: Can not issue SELECT via executeUpdate() or executeLargeUpdate().
//            String sql = "DELETE FROM employees WHERE age = 23;";

            // 3. 执行SQL语句
            int i = statement.executeUpdate(sql);
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4. 关闭Statement对象
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // 5. 关闭连接
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 通过的SQL更新方法
     * 包括：insert, update, delete
     *
     * @param sql: sq语句
     */
    private void update(String sql) {
        Connection conn = JdbcUtils.getConnection();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(statement, conn);
        }
    }

    @Test
    public void test3() {
        String sql = "INSERT INTO employees (`name`, age) VALUES ('Bruny', 22);";
        update(sql);
    }

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
                String name = rs.getNString("name");
                int age = rs.getInt(3);
                System.out.printf("id:%s, name:%s, age:%s\n", id, name, age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(rs, statement, conn);
        }
    }
}

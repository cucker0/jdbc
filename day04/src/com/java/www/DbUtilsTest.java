package com.java.www;

import com.java.exe.Employee;
import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DbUtils
 *
 *
 * 1. ResultSetHandler 的作用: QueryRunner 的 query 方法的返回值最终取决于
 * query 方法的 ResultHandler 参数的 hanlde 方法的返回值.
 *
 * 2. BeanListHandler: 把结果集转为一个 Bean 的 List, 并返回. Bean 的类型在
 * 创建 BeanListHanlder 对象时以 Class 对象的方式传入. 可以适应列的别名来映射
 * JavaBean 的属性名:
 * String sql = "SELECT id, name customerName, email, birth " +
 * 		"FROM customers WHERE id = ?";
 *
 * BeanListHandler(Class<T> type)
 *
 * 3. BeanHandler: 把结果集转为一个 Bean, 并返回. Bean 的类型在创建 BeanHandler
 * 对象时以 Class 对象的方式传入
 * BeanHandler(Class<T> type)
 *
 * 4. MapHandler: 把结果集转为一个 Map 对象, 并返回. 若结果集中有多条记录, 仅返回
 * 第一条记录对应的 Map 对象. Map 的键: 列名(而非列的别名), 值: 列的值
 *
 * 5. MapListHandler: 把结果集转为一个 Map 对象的集合, 并返回.
 * Map 的键: 列名(而非列的别名), 值: 列的值
 *
 * 6. ScalarHandler: 可以返回指定列的一个值或返回一个统计函数的值.
 *
 * 7. QueryLoader
 *      可以用来加载存放着 SQL 语句的资源文件.
 *      使用该类可以把 SQL 语句外置化到一个资源文件中. 以提供更好的解耦
 */
public class DbUtilsTest {
    /**
     * 测试QueryRunner类的 insert方法
     *
     * 执行INSERT SQL语句，可获取主键值
     */
    @Test
    public void testQueryRunnerInsert() {
        QueryRunner queryRunner = new QueryRunner();
        Connection conn = JdbcUtils.getConnection();
        String sql = "INSERT INTO employees (`name`, age, passwd) VALUES (?, ?, ?);";
        try {
            // 获取插入记录是的自增主键值
            BigInteger id = queryRunner.insert(conn, sql, new ScalarHandler<>(), "Karry", 22, "kr123");
            System.out.println(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试QueryRunner类的 update方法
     *
     * update方法可用于更新操作，INSERT、UPDATE、DELETE
     */
    @Test
    public void testQueryRunnerUpdate() {
        // 1. 创建QueryRunner对象
        QueryRunner queryRunner = new QueryRunner();
        String sql = "INSERT INTO employees (`name`, age, passwd) VALUES (?, ?, ?);";

        Connection conn = null;
        try {
            // 2. 获取Connection连接对象
            conn = C3p0Utils.getConnection();
//            conn = DbcpUtils.getConnection();
            // 3. 调用QueryRunner对象的update方法
            int rows = queryRunner.update(conn, sql, "Macker", 22, "mck123");
            System.out.println(rows);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            C3p0Utils.release(conn);
        }
    }

    /**
     * QueryRunner类的query方法
     */
    @Test
    public void testQueryRunnerUpdateQuery() {
        Connection conn = DbcpUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees LIMIT ?;";
        ResultSetHandler resultSetHandler = new ResultSetHandler() {
            @Override
            public List<Employee> handle(ResultSet resultSet) throws SQLException {
                List<Employee> list = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    int age = resultSet.getInt(3);
                    String password = resultSet.getString(4);

                    Employee e = new Employee(id, name, age, password);
                    list.add(e);
                }
                return list;
            }
        };
        try {
            Object object= queryRunner.query(conn, sql, resultSetHandler, 3);
            System.out.println(object);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpUtils.release(conn);
        }
    }

    /**
     * 测试 ResultSetHandler 的 BeanListHandler 实现类
     * BeanListHandler: 把结果集转为一个 Bean 的 List. 该 Bean
     * 的类型在创建 BeanListHandler 对象时传入:
     *
     * new BeanListHandler<>(Employee.class)
     */
    @Test
    public void testBeanListHandler() {
        Connection conn = DbcpUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees LIMIT ?;";
        Object object = null;
        try {
            object = queryRunner.query(conn, sql, new BeanListHandler<>(Employee.class), 10);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpUtils.release(conn);
        }
        System.out.println(object);
    }

    @Test
    public void testMapHandler() {
        Connection conn = JdbcUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees WHERE id = ?;";
        try {
            Map<String, Object> map = queryRunner.query(conn, sql, new MapHandler(), 1);
            System.out.println(map);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(conn);
        }
    }

    @Test
    public void testMapListHandler() {
        Connection conn = JdbcUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees LIMIT ?;";
        try {
            List<Map<String, Object>> mapList = queryRunner.query(conn, sql, new MapListHandler(), 3);
            System.out.println(mapList);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(conn);
        }
    }

    @Test
    public void testScalarHandler() {
        Connection connection = JdbcUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees WHERE id = ?;";
        try {
            Object age = queryRunner.query(connection, sql, new ScalarHandler<>("age"), 4);
            System.out.println(age);
            // new ScalarHandler<>()返回columnIndex
            int index = queryRunner.query(connection, sql, new ScalarHandler<>(), 4);
            System.out.println(index);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(connection);
        }
    }

    /**
     * QueryLoader
     *
     * 可以用来加载存放着 SQL 语句的资源文件.
     * 使用该类可以把 SQL 语句存放到一个资源文件中. 以提供更好的解耦
     */
    @Test
    public void testQueryLoader() {
        // 读取sql.properties里设置的sql语句k/v对
        Map<String, String> sqls = null; // /:表示 classpath根路径
        try {
            sqls = QueryLoader.instance().load("/sql.properties");
            String sql = sqls.get("UPDATE_CUSTOMER");
            System.out.println(sql);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

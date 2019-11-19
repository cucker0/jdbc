package www;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementTest {
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
            conn = JdbcUtils.getConnection();
            statement = conn.createStatement();

            // 2. 准备插入语句
            String sql = "INSERT INTO employees (`name`, age) VALUES ('Lily', 23);";
//            String sql = "INSERT INTO employees (`name`, age) VALUES ('Marry', 28);";
//            String sql = "INSERT INTO employees (`name`, age) VALUES ('Denny', 26);";
//            String sql = "UPDATE employees SET age = 18 WHERE `name` = 'Denny';";
//            String sql = "SELECT * FROM employees;"; // 报异常：SQLException: Can not issue SELECT via executeUpdate() or executeLargeUpdate().
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
     * 包括：insert, update, delete，建表，建库等
     *
     * @param sql: sq语句
     */
    public void update(String sql) {
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
    public void test1() {
//        String sql = "INSERT INTO employees (`name`, age) VALUES ('Bruny', 22);";
        String sql = "create table tuser (id int, address varchar(64));";
        update(sql);
    }
}

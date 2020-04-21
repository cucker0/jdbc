package com.java.www;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 访问数据的 DAO 接口
 * 里边定义好访问数据表的各种方法
 *
 */
public interface DAO<T> {
    /**
     * 批量处理的方法
     *
     * 如批量更新
     * @param connection: Connection数据库连接对象
     * @param sql: 可含?占位符的sql语句
     * @param params: ?占位符对应的多个参数
     * @throws SQLException
     */
    int[] batch(Connection connection, String sql, Object[][] params) throws SQLException;

    /**
     * 返回查询的第一条记录的第一个列的值
     *
     * @param connection: Connection数据库连接对象
     * @param sql: 可含?占位符的sql语句
     * @param params: ?占位符对应的多个参数
     * @param <E>: 返回值类
     * @return: 返回E类型的查询值
     * @throws SQLException
     */
    <E> E getForValue(Connection connection, String sql, Object... params) throws SQLException;

    /**
     * 返回sql查询结果转换为 T 的一个List集合
     *
     * @param connection: Connecti
     * @param sql: 可含?占位符的sql语句
     * @param params: ?占位符对应的多个参数
     * @return
     * @throws SQLException
     */
    List<T> getForList(Connection connection, String sql, Object... params) throws SQLException;

    /**
     * 返回sql查询第一条记录的结果转换为 T 的一个对象
     *
     * @param connection: Connecti
     * @param sql: 可含?占位符的sql语句
     * @param params: ?占位符对应的多个参数
     * @return
     * @throws SQLException
     */
    T getTheFirstRecord(Connection connection, String sql, Object... params) throws SQLException;

    /**
     * sql语句的更新操作
     *
     * @param connection: Connecti
     * @param sql: 可含?占位符的sql语句
     * @param params: ?占位符对应的多个参数
     * @return: 执行sql语句受影响的行数
     * @throws SQLException
     */
    int update(Connection connection, String sql, Object... params) throws SQLException;
}

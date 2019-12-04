package com.bookmall.dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 基本操作的Dao
 */
public abstract class BaseDao<T> {
    private QueryRunner queryRunner = new QueryRunner();
    private Class<T> type;

    // 构造器
    public BaseDao() {
        // 获取子类的类型
        Class clazz = this.getClass();
        // 获取父类的类型
        // getGenericSuperclass() 用来获取当前类的父类的类型
        // ParameterizedType 表示的是带泛型的类型
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        // 获取具体的泛型类型 getActualTypeArguments获取具体的泛型的类型
        // 这个方法会返回一个Type的数组
        Type[] types = parameterizedType.getActualTypeArguments();
        // 获取具体的泛型的类型·
        this.type = (Class<T>) types[0];
    }

    /**
     * 通用的SQL更新操作(INSERT,UPDATE,DELETE)
     *
     * @param conn: Connection连接对象
     * @param sql: 可含?占位符的SQL语句
     * @param params: ?占位符对应的可变参数
     * @return: 执行sql语句受影响的行数
     */
    // 方法
    public int update(Connection conn, String sql, Object... params) {
        int rows = 0;
        try {
            rows = queryRunner.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 执行插入记录，并获取自增ID值
     *
     * @param conn: Connection连接对象
     * @param sql: 可含?占位符的SQL语句
     * @param params: ?占位符对应的可变参数
     * @return: 自增ID值
     */
    public BigInteger insert(Connection conn, String sql, Object... params) {
        BigInteger auto_increment_id = null;
        try {
            auto_increment_id = queryRunner.insert(conn, sql, new ScalarHandler<>(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auto_increment_id;
    }

    /**
     * 执行查询SQL获取第一行，并转为一个Bean对象
     *
     * @param conn: Connection连接对象
     * @param sql: 可含?占位符的SQL语句
     * @param params: ?占位符对应的可变参数
     * @return: T的Bean对象
     */
    public T getBean(Connection conn, String sql, Object... params) {
        T t = null;
        try {
            t = queryRunner.query(conn, sql, new BeanHandler<T>(type), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 执行查询SQL获取所有行，每行并转为转为一个Bean对象，这些Bean对象组成一个List并返回
     *
     * @param conn: Connection连接对象
     * @param sql: 可含?占位符的SQL语句
     * @param params: ?占位符对应的可变参数
     * @return: T的Bean对象组成的List
     */
    public List<T> getBeanList(Connection conn, String sql, Object... params) {
        List<T> list = null;
        try {
            list = queryRunner.query(conn, sql, new BeanListHandler<T>(type),params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 执行返回一个值的查询SQL语句
     *
     * 可用来执行像 select count(*) from ...这样的sql语句
     * @param conn: Connection连接对象
     * @param sql: 可含?占位符的SQL语句
     * @param params: ?占位符对应的可变参数
     * @return: 返回查询SQL执行后返回的单一的值
     */
    public <E> E getValue(Connection conn, String sql, Object... params) {
        E val = null;
        try {
            val = queryRunner.query(conn, sql, new ScalarHandler<>(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return val;
    }

}

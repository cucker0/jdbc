package com.bookmall.dao;

import com.bookmall.Utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * 基本操作的Dao
 *
 * @param <T>: 类型
 */
public abstract class BaseDao<T> {
    private QueryRunner queryRunner;
    // 泛型T的类型
    private Class<T> type;

    // 构造器
    public BaseDao() {
        queryRunner = new QueryRunner();

        // 获取T的类型
        Class clazz = this.getClass();
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types.length > 0) {
            this.type = (Class<T>) types[0];
        } else {
            this.type = null;
        }
    }

    // 方法

    /**
     * 获取数据库连接
     * @return
     */
    protected Connection getConnection() {
        return JdbcUtils.getConnection();
    }

    /**
     * 释放数据库连接
     * @param conn
     */
    protected void release(Connection conn) {
        JdbcUtils.release(conn);
    }

    /**
     * 通用的SQL更新操作(INSERT,UPDATE,DELETE)，适用于事务
     *
     * @param conn: Connection连接对象。
     * @param sql: 可含?占位符的SQL语句
     * @param params: ?占位符对应的可变参数
     * @return: 执行sql语句受影响的行数
     *
     * 注意：需要传入Connection连接对象的方法，适用于事务，Connection在提交事务或回滚事务时释放回连接池。下同
     */
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
     * 通用的SQL更新操作(INSERT,UPDATE,DELETE)，适用于非事务
     *
     * @param sql
     * @param params
     * @return
     *
     * 注意：不需要传入Connection连接对象的方法，适用于非事务，没事执行sql语句前，从连接池获取连接，执行完sql后，在释放回连接池。下同
     */
    public int update(String sql, Object... params) {
        Connection conn = getConnection();
        int rows = update(conn, sql, params);
        release(conn);
        return rows;
    }

    /**
     * 执行插入一条记录，并获取自增ID值
     *
     * @param conn: Connection连接对象
     * @param sql: 可含?占位符的SQL语句
     * @param params: ?占位符对应的可变参数
     * @return: 自增ID值
     */
    public Integer insert(Connection conn, String sql, Object... params) {
        BigInteger auto_increment_id = null;
        try {
            auto_increment_id = queryRunner.insert(conn, sql, new ScalarHandler<>(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auto_increment_id == null ? null : auto_increment_id.intValue();

    }

    public Integer insert(String sql, Object... params) {
        Connection conn = getConnection();
        Integer auto_id = insert(conn, sql, params);
        release(conn);
        return auto_id;
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
        T bean = null;
        try {
            bean = queryRunner.query(conn, sql, new BeanHandler<T>(type), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public T getBean(String sql, Object... params) {
        Connection conn = getConnection();
        T bean = getBean(conn, sql, params);
        release(conn);
        return bean;
    }

    /**
     * 执行查询SQL获取所有行，每行并转为转为一个Bean对象，这些Bean对象组成一个List并返回
     *
     * @param conn:   Connection连接对象
     * @param sql:    可含?占位符的SQL语句
     * @param params: ?占位符对应的可变参数
     * @return: T的Bean对象组成的List
     */
    public List<T> getBeanList(Connection conn, String sql, Object... params) {
        List<T> list = null;
        try {
            list = queryRunner.query(conn, sql, new BeanListHandler<T>(type), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<T> getBeanList(String sql, Object... params) {
        Connection conn = getConnection();
        List<T> list =  getBeanList(conn, sql, params);
        release(conn);
        return list;
    }

    /**
     * 执行返回一个值的SQL查询语句
     *
     * 可用来执行像 select count(*) from ...这样的sql语句
     *
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

    public <E> E getValue(String sql, Object... params) {
        Connection conn = getConnection();
        E value = getValue(conn, sql, params);
        release(conn);
        return value;
    }
}

package com.java.www;

import com.java.exe.ReflectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 使用QueryRunner 提供具体的实现
 *
 * 使用时，闯将使用匿名类实例，如：
 * DaoJdbcImpl<Employee> dao = new DaoJdbcImpl<>() {};
 *
 * @param <T>: 子类需传入的泛型类型
 */
public class DaoJdbcImpl<T> implements DAO<T> {
    private QueryRunner queryRunner;
    private Class<T> type;

    // 构造器
    public DaoJdbcImpl() {
        queryRunner = new QueryRunner();
        type = ReflectionUtils.getSuperGenericType(getClass());
    }

    // 方法
    @Override
    public int[] batch(Connection connection, String sql, Object[]... args) throws SQLException {
        return queryRunner.batch(connection, sql, args);
    }

    @Override
    public <E> E getForValue(Connection connection, String sql, Object... args) throws SQLException {
        return queryRunner.query(connection, sql, new ScalarHandler<>(), args);
    }

    @Override
    public List<T> getForList(Connection connection, String sql, Object... args) throws SQLException {
        return queryRunner.query(connection, sql, new BeanListHandler<>(type), args);
    }

    @Override
    public T getTheFirstRecord(Connection connection, String sql, Object... args) throws SQLException {
        return queryRunner.query(connection, sql, new BeanHandler<>(type), args);
    }

    @Override
    public int update(Connection connection, String sql, Object... args) throws SQLException {
        return queryRunner.update(connection, sql, args);
    }
}

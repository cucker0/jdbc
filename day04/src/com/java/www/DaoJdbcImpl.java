package com.java.www;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DaoJdbcImpl<T> implements DAO<T> {
    @Override
    public void batch(Connection connection, String sql, Object... args) throws SQLException {

    }

    @Override
    public <E> E getForValue(Connection connection, String sql, Object... args) throws SQLException {
        return null;
    }

    @Override
    public List<T> getForList(Connection connection, String sql, Object... args) throws SQLException {
        return null;
    }

    @Override
    public T getTheFirstRecord(Connection connection, String sql, Object... args) throws SQLException {
        return null;
    }

    @Override
    public int update(Connection connection, String sql, Object... args) throws SQLException {
        return 0;
    }
}

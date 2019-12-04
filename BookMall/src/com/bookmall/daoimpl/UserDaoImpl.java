package com.bookmall.daoimpl;

import com.bookmall.beans.User;
import com.bookmall.dao.BaseDao;
import com.bookmall.dao.UserDao;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;

public class UserDaoImpl extends BaseDao<User> implements UserDao {
    @Override
    public User getUser(Connection conn, User user) {
        User bean = null;
        String sql = "SELECT id, username, password, email FROM users WHERE username = ? AND password = ?;";
        bean = getBean(conn, sql, user.getUsername(), user.getPassword());
        return bean;
    }

    @Override
    public boolean checkUsername(Connection conn, User user) {
        User bean = null;
        String sql = "SELECT id, username, password, email FROM users WHERE username = ?;";
        bean = getBean(conn, sql, user.getUsername());
        return bean != null;
    }

    @Override
    public BigInteger saveUser(Connection conn, User user) {
        BigInteger auto_increment_id = null;
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?);";
        auto_increment_id = insert(conn, sql, user.getUsername(), user.getPassword(), user.getEmail());
        return auto_increment_id;
    }

    public boolean login(Connection conn, User user) {
        User bean = null;
        String sql = "SELECT id, username, password, email FROM users WHERE username = ? AND password = ?;";
        bean = getBean(conn, sql, user.getUsername(), user.getPassword());
        return bean != null;
    }
}

package com.bookmall.dao;

import com.bookmall.beans.User;

import java.math.BigInteger;
import java.sql.Connection;

public interface UserDao {
    User getUser(Connection conn, User user);

    /**
     * 检查用户是否存在数据库表中
     * @param conn
     * @param user
     * @return
     */
    boolean checkUsername(Connection conn, User user);

    BigInteger saveUser(Connection conn, User user);
}

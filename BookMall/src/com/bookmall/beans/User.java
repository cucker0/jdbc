package com.bookmall.beans;

import com.bookmall.Utils.Sha1;
import org.apache.commons.codec.digest.DigestUtils;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;

    // 构造器
    public User() {
    }

    // 方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * 使用sha1加密
     * @param password 明文密码
     */
    public void setPassword(String password) {
        String sign = DigestUtils.sha1Hex(password);
//        String sign = Sha1.getSha1(password);
        this.password = sign;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

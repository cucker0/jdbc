package com.java.exe;

import org.junit.Test;

import java.io.*;
import java.sql.*;

public class JdbcTest {
    @Test
    public void testGetKeyValue() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "INSERT INTO employees (`name`, age) VALUES (?, ?);";
            preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, "朱迟");
            preparedStatement.setObject(2, "30");

            int rows = preparedStatement.executeUpdate();

            /*
            * 插入新纪录时，通过 getGeneratedKeys() 获取包含了新生成的主键的 ResultSet 对象
            * 在 ResultSet 中只有一列 GENERATED_KEY, 用于存放新生成的主键值.
            * */
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                System.out.println(resultSet.getObject(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }
    }

    /**
     * 通过JDBC插入BLOB型数据到数据库中
     * 插入 BLOB 类型的数据必须使用 PreparedStatement：因为 BLOB 类型的数据无法使用字符串拼写的
     * 插入的对象必须为InputStream输入流对象
     *
     * mysql 4中BLOB类型
     * 类型       最大空间
     * TinyBlob 255 Byte
     * Blob 65 KB
     * MEDIUMBLOB 16MB
     * LONGBLOB 4GB
     *
     * 超过最大容量报异常：Data truncation: Data too long for column 'XX' at row 1
     */
    @Test
    public void testInsertBlob() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "INSERT INTO employees (`name`, age, profile_picture) VALUES (?, ?, ?);";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1,"赵茜");
            preparedStatement.setObject(2,23);
            // 提供一个InputStream对象，这里用文件输入流
            InputStream inputStream = new FileInputStream("头像1.png");
            preparedStatement.setObject(3,inputStream);
            int rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(preparedStatement, conn);
        }
    }

    /**
     * 读取BLOB类型数据
     *
     * 1. 先从结果集中获取Blob对象，
     * 2. 再调用Blob对象的getBinaryStream()方法，获取输入流
     * 3. 读取输入流字节，通过输出流写到文件
     */
    @Test
    public void readBlob() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT id, `name`, age, passwd AS `password`, profile_picture FROM employees WHERE id = ?;";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1, 19);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int age = resultSet.getInt(3);
                String password = resultSet.getString(4);
                Blob profile_picture = resultSet.getBlob(5);

                System.out.printf("id: %s, name: %s, age: %s, password: %s\n", id, name, age, password);
                // 获取Blob对象的二进制输入流
                InputStream is = profile_picture.getBinaryStream();
//                System.out.println(is.available());

                // 创建一个文件输出流
                OutputStream os = new FileOutputStream("getAvatar.png");
                // 读取Blob对象的二进制输入流数据，写到文件输出流
                byte[] buf = new byte[1024 * 8];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.close();
                is.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        JdbcUtils.release(resultSet, preparedStatement, conn);
    }
}

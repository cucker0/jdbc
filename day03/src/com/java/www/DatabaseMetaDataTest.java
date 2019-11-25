package com.java.www;

import org.junit.Test;

import java.sql.*;

/**
 * DatabaseMetaData 类
 * 描述结果集的元数据
 * 提供了许多方法用于获得数据源的各种信息
 *
 * ## 方法
 * getURL()：返回一个String类对象，代表数据库的URL。
 * getUserName()：返回连接当前数据库管理系统的用户名。
 * isReadOnly()：返回一个boolean值，指示数据库是否只允许读操作。
 * getDatabaseProductName()：返回数据库的产品名称。
 * getDatabaseProductVersion()：返回数据库的版本号。
 * getDriverName()：返回驱动驱动程序的名称。
 * getDriverVersion()：返回驱动程序的版本号
 */
public class DatabaseMetaDataTest {
    @Test
    public void testResultSetMetaData() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT FlowID flowID, `Type` TYPE, IDCard idCard, ExamCard examCard, StudentName studentName, Location location, Grade grade " +
                    "FROM examstudent ";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            // 获取ResultSetMetaData结果集元数据对象
            ResultSetMetaData rsmd = resultSet.getMetaData();
            // 获取结果集的列数量
            int columnCount = rsmd.getColumnCount();
            System.out.println("columnCount: " + columnCount);

            for (int i = 0; i < columnCount; ++i) {
                // 获取列名
                String columnName = rsmd.getColumnName(i + 1);
                // 获取列别名(标识名)
                String columnLable = rsmd.getColumnLabel(i + 1);
                System.out.printf("columnName: %s, columnLable:%s\n", columnName, columnLable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }
    }

}

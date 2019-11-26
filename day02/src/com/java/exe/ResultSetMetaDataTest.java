package com.java.exe;

import org.junit.Test;

import java.sql.*;


/**
 * ResultSetMetaData类
 * 获取关于 ResultSet 对象中列的类型和属性信息的对象
 *
 * getColumnName(int column)：获取指定列的名称
 * getColumnCount()：返回当前 ResultSet 对象中的列数。
 * getColumnTypeName(int column)：检索指定列的数据库特定的类型名称。
 * getColumnDisplaySize(int column)：指示指定列的最大标准宽度，以字符为单位。
 * isNullable(int column)：指示指定列中的值是否可以为 null。
 * isAutoIncrement(int column)：指示是否自动为指定列进行编号，这样这些列仍然是只读的。
 */
public class ResultSetMetaDataTest {
    @Test
    public void test() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees WHERE id = ?;";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, 7);
            resultSet = preparedStatement.executeQuery();

            Class clazz = Employee.class;
            Object object = clazz.newInstance();
            // 得到ResultSetMetaData对象，即结果集的列名、列标示名，列数量等
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                for (int i = 0; i < resultSetMetaData.getColumnCount(); ++i) {
                    String columnLabel = resultSetMetaData.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);

                    // 把columnLabel作为对象的field名，columnValue作为field的值
                    ReflectionUtils.setFieldValue(object, columnLabel, columnValue);
                }
            }
            System.out.println(object);


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }

    }
}

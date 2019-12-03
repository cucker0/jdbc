package com.java.www;

import com.java.exe.Employee;
import com.java.exe.ReflectionUtils;
import org.junit.Test;

import java.sql.*;
import java.sql.ResultSetMetaData;

/**
 * ResultSetMetaData 接口
 * 描述结果集的元数据
 * 可以得到结果集中的基本信息: 结果集中有哪些列, 列名, 列的别名等
 * 由resultSet.getMetaData()获取ResultSetMetaData对象
 *
 * ## 方法
 * int getColumnCount() 获取列总数
 * String getColumnName(int column) 通过索引获取列名
 * String getColumnLabel(int column) 通过索引获取列别名(列标示名)
 * getColumnTypeName(int column)：检索指定列的数据库特定的类型名称。
 * getColumnDisplaySize(int column)：指示指定列的最大标准宽度，以字符为单位。
 * isNullable(int column)：指示指定列中的值是否可以为 null。
 * isAutoIncrement(int column)：指示是否自动为指定列进行编号，这样这些列仍然是只读的
 */
public class ResultSetMetaDataTest {
    @Test
    public void testResultSetMetaData() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = com.java.exe.JdbcUtils.getConnection();
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
            com.java.exe.JdbcUtils.release(resultSet, preparedStatement, conn);
        }

    }

    @Test
    public void testResultSetMetaData2() {
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

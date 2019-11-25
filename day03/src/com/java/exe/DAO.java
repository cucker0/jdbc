package com.java.exe;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO {
    /**
     * 更新操作
     *
     * insert, update, delete等更新sql语句的执行
     *
     * @param sql: 可含?占位参数sql语句
     * @param args: ?占位参数
     * @return: 受影响的行数
     */
    public int update(String sql, Object... args) {
        int rows = 0;
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = JdbcUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            // 设置?占位符参数
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            // 执行sql
            rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(preparedStatement, conn);
        }
        return rows;
    }

    /**
     * 获取结果集的ColumnLabel组成的List
     *
     * @param resultSet: ResultSet结果集对象
     * @return: ColumnLabel组成的List对象
     */
    public List<String> getColumnLables(ResultSet resultSet) {
        List<String> columnLables = new ArrayList<>();
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            for (int i = 0; i < rsmd.getColumnCount(); ++i) {
                columnLables.add(rsmd.getColumnLabel(i + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnLables;
    }

    /**
     * 处理结果集
     *
     * @param resultSet: 结果集
     * @return: 返回一个由Map元素组成的List，一各Map对位对饮一条记录，格式：[{ }, { } ...]
     */
    public List<Map<String, Object>> handleResultSetToMapList(ResultSet resultSet) {
        List<Map<String, Object>> values = new ArrayList<>();
        List<String> columnLables = getColumnLables(resultSet);
        Map<String, Object> map = null;

        try {
            while (resultSet.next()) {
                // 一个map封装一条记录
                map = new HashMap<>();
                for (String columnLable : columnLables) {
                    Object value = resultSet.getObject(columnLable);
                    map.put(columnLable, value);
                }
                // 处理好的Map对象添加到values List中
                values.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }

    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
        List<T> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = JdbcUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.executeQuery();
            List<Map<String, Object>> values = handleResultSetToMapList(resultSet);
            list = transfterMapListToBeanList(clazz, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }
        return list;
    }

    public <T> List<T> transfterMapListToBeanList(
            Class<T> clazz,
            List<Map<String, Object>> values
    ) {
        List<T> result = new ArrayList<>();
        try {
            if (values.size() > 0) {
                for (Map<String, Object> map : values) {
                    T bean = clazz.newInstance();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String propertyName = entry.getKey();
                        Object value = entry.getValue();
                        BeanUtils.setProperty(bean, propertyName, value);
                    }
                    result.add(bean);
                }

            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

}

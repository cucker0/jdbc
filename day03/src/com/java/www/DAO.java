package com.java.www;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object工具类
 *
 * 功能:
 * sql语句更新操作
 * 获取sql语句执行查询结果集的第一条记录的第一个字段的值
 * 传入 SQL 语句和 Class 对象，从sql语句的查询结果集中获取第一记录对应的Class类的对象实例
 * 传入 SQL 语句和 Class 对象, 返回 SQL 语句查询到的所有记录对应的 Class 类的对象的集合
 */
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
     * 获取指定查询sql语句的查询结果集第一条记录的第一个字段的值
     * 还可以获取标量查询的结果，即只返回一个查询值，如查询记录行数
     *
     * @param sql: sql语句
     * @param args: sql语句中的?点位符对应的参数
     * @param <E>: 返回值对应的类型
     * @return: 查询到的一个值
     *      注意：查询 COUNT(*) 值时，需要用Object 类型的变量来接收值
     */
    public <E> E getForValue(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = JdbcUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();
            // 有结果则返回第一条记录的第一个列的值
            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }
        return null;
    }

    /**
     * 传入 SQL 语句和 Class 对象，从sql语句的查询结果集中获取第一记录对应的Class类的对象实例
     *
     * @param clazz: Class对象
     * @param sql: sql语句
     * @param args: sql语句中的?点位符对应的参数
     * @param <T>: 泛型类型
     * @return: Class对应类的对象实例
     */
    public <T> T getTheFirstRecord(Class<T> clazz, String sql, Object... args) {
        List<T> result = getForList(clazz, sql, args);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * 传入 SQL 语句和 Class 对象, 返回 SQL 语句查询到的所有记录对应的 Class 类的对象的集合
     *
     * @param clazz: Class对象
     * @param sql: sql语句
     * @param args: sql语句中的?点位符参数
     * @param <T>: 泛型类型
     * @return: Class 类的对象的集合
     */
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
            resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> values = handleResultSetToMapList(resultSet);
            list = transfterMapListToBeanList(clazz, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, conn);
        }
        return list;
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

    /**
     * 把MapList转换成BeanList
     *
     * @param clazz: 类型
     * @param values: 把MapList转换成BeanList对象
     * @param <T>: 泛型类型
     * @return: 转换处理后的BeanList对象
     */
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

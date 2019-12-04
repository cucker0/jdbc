package com.bookmall.daoimpl;

import com.bookmall.Utils.CommonUtils;
import com.bookmall.beans.Order;
import com.bookmall.dao.BaseDao;
import com.bookmall.dao.OrderDao;

import java.math.BigInteger;
import java.sql.Connection;

public class OrderDaoImpl extends BaseDao<Order> implements OrderDao {
    @Override
    public Order getOrderById(Connection conn, String orderId) {
        Order bean = null;
        String sql = "SELECT id, order_time orderTime, total_count totalCount, total_amount totalAmount, state, user_id userId FROM orders WHERE id = ?;";
        bean = getBean(conn, sql, orderId);
        return bean;
    }

    @Override
    public Order getOrderByUserId(Connection conn, String userId) {
        Order bean = null;
        String sql = "SELECT id, order_time orderTime, total_count totalCount, total_amount totalAmount, state, user_id userId FROM orders WHERE user_id = ?";
        bean = getBean(conn, sql, userId);
        return bean;
    }

    @Override
    public int saveOrder(Connection conn, Order order) {
//        BigInteger auto_increment_id = BigInteger.valueOf(0);
        int rows = 0;
        String sql = "INSERT INTO orders (order_time, total_count, total_amount, state, user_id) VALUES (?, ?, ?, ?, ?, ?);";
//        rows = update(conn, sql, CommonUtils.localDateTimeTransformString(order.getOrderTime()), order.getTotalCount(),
//                order.getTotalAmount(), order.getState(), order.getUserId());
        rows = update(conn, sql,order.getOrderTime(), order.getTotalCount(),
                order.getTotalAmount(), order.getState(), order.getUserId());
        return rows;
    }

    @Override
    public void deleteOrderById(Connection conn, String orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";
        update(conn, sql, orderId);
    }
}

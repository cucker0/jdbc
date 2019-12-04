package com.bookmall.dao;

import com.bookmall.beans.Order;

import java.sql.Connection;

public interface OrderDao {
    Order getOrderById(Connection conn, String orderId);
    Order getOrderByUserId(Connection conn, String userId);
    int saveOrder(Connection conn, Order order);
    void deleteOrderById(Connection conn, String orderId);
}

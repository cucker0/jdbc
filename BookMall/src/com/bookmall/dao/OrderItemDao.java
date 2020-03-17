package com.bookmall.dao;

import com.bookmall.beans.OrderItem;

import java.math.BigInteger;
import java.sql.Connection;

public interface OrderItemDao {
    OrderItem getOrderItemById(Connection conn, int orderItemId);
    OrderItem getOrderItemByOrderId(Connection conn, String orderId);
    Integer saveOrderItem(Connection conn, OrderItem orderItem);
    void deleteOrderItemById(Connection conn, String orderItemId);
    void deleteOrderItemByOrderId(Connection conn, String orderId);
}

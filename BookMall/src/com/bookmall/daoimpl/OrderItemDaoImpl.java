package com.bookmall.daoimpl;

import com.bookmall.beans.Order;
import com.bookmall.beans.OrderItem;
import com.bookmall.dao.BaseDao;
import com.bookmall.dao.OrderItemDao;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;

public class OrderItemDaoImpl extends BaseDao<OrderItem> implements OrderItemDao {

    @Override
    public OrderItem getOrderItemById(Connection conn, int orderItemId) {
        OrderItem bean = null;
        String sql = "SELECT `id`, `count`,`amount`,`title`,`author`,`price`,`img_path` imgPath,`order_id` orderId FROM order_items WHERE id = ?;";
        bean = getBean(conn, sql, orderItemId);
        return bean;
    }

    @Override
    public OrderItem getOrderItemByOrderId(Connection conn, String orderId) {
        OrderItem bean = null;
        String sql = "SELECT `id`, `count`,`amount`,`title`,`author`,`price`,`img_path` imgPath, `order_id` as orderId FROM order_items WHERE order_id = ?;";
        bean = getBean(conn, sql, orderId);
        return bean;
    }

    public OrderItem getOrderItemByOrderId(Connection conn, Order order) {
        if (order.getId() == null) {
            return null;
        }
        return getOrderItemByOrderId(conn, order.getId());
    }

    @Override
    public Integer saveOrderItem(Connection conn, OrderItem orderItem) {
        Integer auto_increment_id = null;
        String sql = "INSERT  INTO order_items (`count`, `amount`, `title`, `author`, `price`, `img_path`, `order_id`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        auto_increment_id = insert(conn, sql, orderItem.getCount(), orderItem.getAmount(),
                orderItem.getTitle(), orderItem.getAuthor(), orderItem.getPrice(), orderItem.getImgPath(), orderItem.getOrderId());
        return auto_increment_id;
    }

    @Override
    public void deleteOrderItemById(Connection conn, String orderItemId) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        update(conn, sql, orderItemId);
    }

    @Override
    public void deleteOrderItemByOrderId(Connection conn, String orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        update(conn, sql, orderId);
    }
}

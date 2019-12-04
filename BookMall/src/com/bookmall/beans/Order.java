package com.bookmall.beans;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Order {
    private String id;
    private LocalDateTime orderTime;
    private int totalCount;
    private double totalAmount;
    private Integer state;
    private Integer userId;

    // 构造器
    public Order() {}

    // 方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Object orderTime) {
//        this.orderTime = orderTime;
        System.out.println("====>");
        Timestamp tp = (Timestamp) orderTime;
        this.orderTime = tp.toLocalDateTime();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", orderTime=" + orderTime +
                ", totalCount=" + totalCount +
                ", totalAmount=" + totalAmount +
                ", state=" + state +
                ", userId=" + userId +
                '}';
    }
}

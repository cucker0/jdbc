package com.bookmall.beans;

import java.time.LocalDateTime;

public class Order {
    private String id;
    private LocalDateTime orderTime;
    private int totalCount;
    private double totalAmount;
    private int state;
    private int userId;

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

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

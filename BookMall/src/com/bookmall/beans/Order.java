package com.bookmall.beans;

import java.sql.Timestamp;

public class Order {
    private String id;
    private Timestamp orderTime; // 或String类型，都可与sql中的datetime对应
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

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
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

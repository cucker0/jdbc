package com.bookmall.beans;

import java.util.List;

public class Page<T> {
    private List<T> list;
    // 每页显示的条数
    public static final int PAGE_SIZE = 10;
    private int pageNo;
    private int totalRecord;

    // 构造器
    public Page() {
    }

    // 方法
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    @Override
    public String toString() {
        return "Page{" +
                "list=" + list +
                ", pageNo=" + pageNo +
                ", totalRecord=" + totalRecord +
                '}';
    }
}

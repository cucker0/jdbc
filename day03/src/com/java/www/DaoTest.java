package com.java.www;

import com.java.exe.Employee;
import org.junit.Test;

import java.util.List;

public class DaoTest {
    DAO dao = new DAO();

    @Test
    public void testUpdate() {
        String sql = "INSERT INTO employees (`name`, age) VALUES (?, ?);";
        int rows = dao.update(sql, "马云", 20);
        System.out.println(rows);
    }

    @Test
    public void testGetForValue() {
        String sql = "SELECT COUNT(*) FROM employees;";
        Object c = dao.getForValue(sql);
        System.out.println("count: " + c);

        sql = "SELECT MAX(age) FROM employees;";
        int maxAge = dao.getForValue(sql);
        System.out.println("maxAge: " + maxAge);

        sql = "SELECT passwd AS `password`FROM employees WHERE name = ?;";
        String pwd = dao.getForValue(sql, "katy");
        System.out.println(pwd);
    }

    @Test
    public void testGetTheFirstRecord() {
        Class<Employee> clazz = Employee.class;
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees WHERE name = ?;";
        Employee e = dao.getTheFirstRecord(clazz, sql, "马云");
        System.out.println(e);
    }

    @Test
    public void testGetForList() {
        Class<Employee> clazz = Employee.class;
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees;";
        List<Employee> list = dao.getForList(clazz, sql);
//        list.forEach(System.out::println);
        System.out.println(list);
    }
}

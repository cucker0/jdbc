package com.java.www;

import com.java.exe.Employee;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class EmployeeDaoTest {
//    DaoEmployee dao = new DaoEmployee();
    // 为了简单，直接用匿名类
    DaoJdbcImpl<Employee> dao = new DaoJdbcImpl<>() {
    };

    @Test
    public void getTheFirstRecord() {
        Connection conn = JdbcUtils.getConnection();
        String sql = "SELECT id, `name`, age, passwd AS `password` FROM employees WHERE id = ?;";
        try {
            Employee e = dao.getTheFirstRecord(conn, sql, 6);
            System.out.println(e);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JdbcUtils.release(conn);
        }
    }
}

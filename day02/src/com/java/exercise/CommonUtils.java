package com.java.exercise;

import com.java.www.JdbcUtils;
import org.junit.Test;


import javax.swing.*;
import java.util.Scanner;


public class CommonUtils {
    private static Scanner sc = new Scanner(System.in);

    @Test
    public void insertDataBatch() {
        String sql = "INSERT INTO examstudent (FlowID, `Type`, IDCard, ExamCard, StudentName, Location, Grade) VALUES (?, ?, ?, ?, ?, ?, ?);";
        String sqlText = "1\t4\t412824195263214584\t200523164754000\t张锋\t郑州\t85\n" +
                "2\t4\t222224195263214584\t200523164754001\t孙朋\t大连\t56\n" +
                "3\t6\t342824195263214584\t200523164754002\t刘明\t沈阳\t72\n" +
                "4\t6\t100824195263214584\t200523164754003\t赵虎\t哈尔滨\t95\n" +
                "5\t4\t454524195263214584\t200523164754004\t杨丽\t北京\t64\n" +
                "6\t4\t854524195263214584\t200523164754005\t王小红\t太原\t60";
        String[] sqlRows = sqlText.split("\n");
        for (String sqlRow : sqlRows) {
            String[] args = sqlRow.split("\t");
            JdbcUtils.update(sql, args);
        }
    }

    @Test
    public void insertOneRecord() {
        String sql = "INSERT INTO examstudent (`Type`, IDCard, ExamCard, StudentName, Location, Grade) VALUES (?, ?, ?, ?, ?, ?);";

        while (true) {
            Student student = new Student();
            System.out.println("== 插入学生信息 ==\n");
            System.out.println("请输入考生的详细信息:");
            System.out.println("Type: (-1 退出)");
            int Type = sc.nextInt();
            if (Type == -1) {
                break;
            }
            System.out.println("IDCard:");
            String IDCard = sc.next();
            System.out.println("ExamCard:");
            String ExamCard = sc.next();
            System.out.println("StudentName:");
            String StudentName = sc.next();
            System.out.println("Location:");
            String Location = sc.next();
            System.out.println("Grade:");
            int Grade = sc.nextInt();

            int row = JdbcUtils.update(sql, Type, IDCard, ExamCard, StudentName, Location, Grade);
            if (row == 1) {
                System.out.println("信息录入成功!");
            }
        }
    }

    @Test
    public void query() {
        Student student = null;
        while (true) {
            System.out.println("\n请输入查询类型：\n" +
                    "        a. 准考证\n" +
                    "        b. 身份证\n" +
                    "        c. 退出\n");
            String type = sc.next();
            switch (type) {
                case "a":
                    System.out.println("输入准考证:");
                    String examCard = sc.next();
                    student = searchByExamCard(examCard);
                    if (student != null) {
                        System.out.println(student);
                    }
                    break;
                case "b":
                    System.out.println("输入身份证:");
                    String idCard = sc.next();
                    student = searchByIdCard(idCard);
                    if (student != null) {
                        System.out.println(student);
                    }
                    break;
                case "c":
                    return;
                default:
                    System.out.println("您的输入有误!");
                    break;
            }

        }
    }

    /**
     * 用身份证号搜索
     *
     * @param idCard
     * @return
     */
    public static Student searchByIdCard(String idCard) {
        Student stu =  null;
        String sql = "SELECT FlowID flowID, Type type, IDCard idCard, ExamCard examCard, StudentName studentName, Location location, Grade grade " +
                "FROM examstudent WHERE IDCard = ?;";
        Class<Student> clazz = Student.class;
        stu = JdbcUtils.get(clazz, sql, idCard);
        if (stu == null) {
            System.out.println("查无此人");
        }
        return stu;
    }

    /**
     * 用准考证号搜索
     *
     * @param examCard
     * @return
     */
    public static Student searchByExamCard(String examCard) {
        Student stu =  null;
        String sql = "SELECT FlowID flowID, `Type` type, IDCard idCard, ExamCard examCard, StudentName studentName, Location location, Grade grade " +
                "FROM examstudent WHERE ExamCard = ?;";
        Class<Student> clazz = Student.class;
        stu = JdbcUtils.get(clazz, sql, examCard);
        if (stu == null) {
            System.out.println("查无此人");
        }
        return stu;
    }


}

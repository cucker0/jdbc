package com.java.www;


import com.java.exe.Employee;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * Java Bean 工具
 *
 * 依赖commons-beanutils、commons-logging jar包
 *
 */
public class BeanUtilsTest {
    @Test
    public void testGetProperty() {
        Object object = new Employee();
        System.out.println(object);

        try {
            BeanUtils.setProperty(object, "age", 24);
            System.out.println(object);

            Object val = BeanUtils.getProperty(object,"age");
            System.out.println("val: " + val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetProperty() {
        Object object= new Employee();
        System.out.println(object);

        try {
            BeanUtils.setProperty(object, "name", "许诺");
            BeanUtils.setProperty(object, "age", 24);
            BeanUtils.setProperty(object, "password", "mima123");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(object);

    }
}

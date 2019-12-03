package com.bookmall.www;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.Scanner;

public class Mylab {
    private static Scanner sc = new Scanner(System.in);

    @Test
    public void test() {
        String str = "123456";
        String sign = DigestUtils.sha1Hex(str);
        System.out.println(sign);
    }

    /**
     * 4. 添加用户
     */
    @Test
    public void addUser() {
        
    }
}

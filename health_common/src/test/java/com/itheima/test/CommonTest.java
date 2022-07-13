package com.itheima.test;

import org.junit.Test;

public class CommonTest {

    @Test
    public void test1(){
        String date = "1996-5";
        String year = date.substring(0,4);  //左闭右开
        String month = date.substring(5);

        System.out.println(year);
        System.out.println(month);
    }
}

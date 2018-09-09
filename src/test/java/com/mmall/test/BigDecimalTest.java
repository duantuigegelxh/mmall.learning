package com.mmall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 */




public class BigDecimalTest {

    @Test
    public void test1(){//输出后面会有一个尾数
        System.out.println(0.05+0.01);
        System.out.println(1.0-0.42);
        System.out.println(4.015*100);
        System.out.println(123.3/100);
    }







    @Test
    public void test2(){//输出后面会有一个尾数
        BigDecimal b1 = new BigDecimal(0.05);
        BigDecimal b2 = new BigDecimal(0.01);
        System.out.println(b1.add(b2));
    }

    @Test
    public void test3(){//这个用了字符串构造方法后才能解决有尾数问题，后面存的时候再转换为double类型
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.01");
        System.out.println(b1.add(b2));

    }

}

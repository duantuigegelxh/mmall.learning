package com.mmall.util;

import java.math.BigDecimal;

/**
 */
public class BigDecimalUtil {

     private BigDecimal(){

     }

    //加法
    public static BigDecimal add(double v1,double v2){
        BigDecimal b1=new BigDecimal(Double.toString());
        BigDecimal b2=new BigDecimal(Double.toString());
        return b1.add(b2);
    }

    //减法
    public static BigDecimal sub(double v1,double v2){
        BigDecimal b1=new BigDecimal(Double.toString());
        BigDecimal b2=new BigDecimal(Double.toString());
        return b1.subtract(b2);
    }

    //乘法
    public static BigDecimal mul(double v1,double v2){
        BigDecimal b1=new BigDecimal(Double.toString());
        BigDecimal b2=new BigDecimal(Double.toString());
        return b1.multiply(b2);
    }

    //除法，会有除不尽的情况
    public static BigDecimal div(double v1,double v2){
        BigDecimal b1=new BigDecimal(Double.toString());
        BigDecimal b2=new BigDecimal(Double.toString());
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);//保留两位小数，采用四舍五入的方法
    }
}















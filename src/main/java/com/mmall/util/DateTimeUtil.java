package com.mmall.util;


import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 *此类作为一个时间转换的类
 */
public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //我们使用joda-time

    //字符串转成date类型
    public static Date strToDate(String dateTimeStr,String formatStr){
        DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(formatStr);
        DateTime datetime=dateTimeFormatter.parseDateTime(dateTimeStr);
        return datetime.toDate();
    }

    //date类型 转为String
    public static String dateToStr(Date date,String formatStr){
        if(date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime datetime=dateTimeFormatter.parseDateTime(dateTimeStr);
        return datetime.toDate();
    }

    //date类型 转为String
    public static String dateToStr(Date date){
        if(date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }



}

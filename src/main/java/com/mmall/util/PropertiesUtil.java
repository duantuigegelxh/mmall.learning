package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *
 */
public class PropertiesUtil {

    private static Logger logger= LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties props;

    //我们需要在tomcat启动的时候读取到page，因此我们需要用到一个静态块来解决这类问题
    static{
        String fileName="mmall.properties";
        props=new Properties();
        try{
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));//读取properties文件
        }catch (IOException e){
            logger.error("配置文件读取异常",e);//当出现异常时，会打印进日志
        }
    }
    public static String getProperty(String key){
        String value=props.getProperty(key.trim());//这是为了我们在写配置的时候，有空格
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();//这是为了我们在写配置的时候，有空格
    }


    public static String getProperty(String key,String defultValue){
        String value=props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value=defultValue;//当传过来的key为空时，返回默认值
        }
        return value.trim();
    }
}













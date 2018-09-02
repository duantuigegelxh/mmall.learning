package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by 刘湘海 on 2018/7/29.
 */
public class TokenCache {

    //先把日志声明一下
    private static Logger logger=LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX="token_";

    //声明一个静态的代码块，这个是瓜娃里面的本地缓存,构建本地的Cache
    //缓存的初始华容量为1000,后面maxmumSize（）方法表示当超过这个容量的时候，会使用LRU算法来移除缓存项
    //expireAfterAccess(12,)表示缓存有效期，12表示12个小时
    private static LoadingCache<String.String> LocalCache= CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12,TimeUnit.HOURS)
            .build(new CacheLoader<String,String>() {
                @Override
                //默认的加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
                public String load(String s) throws Exception {
                    return "null";//变成字符串的key，使得调用这个方法的时候不会出错
                }
            });

    public static void setKey(String key,String value){
        LocalCache.put(key,value);
    }

    public static String getKey(String key){
        String value=null;
        try{
            value=localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch(Exception e){
            logger.error("localCache get error",e);//打印异常信息
             return null;
        }
    }
}







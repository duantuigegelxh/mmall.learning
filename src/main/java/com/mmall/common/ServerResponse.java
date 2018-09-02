package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//使用这个注解，当下面三个成员变量只需要其中几个的时候，那么就不会返回空的。
//比如我们调用了private ServerResponse(int status)，那么msg和data就不会序列化成json
//保证序列化Json的时候，如果是null的对象，key也会消失
public class ServerResponse<T> implements Serializable{//会进行一些序列化，然后返回给前端

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status,T data){
        this.status=status;
        this.data=data;
    }

    private ServerResponse(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }

    @JsonIgnore
    //使用这个注解后，下面这个方法的返回值就不会显示在我们的json里面，而下面三个有get提示的方法则会显示在JSON数据里面
    //使之不在Json序列化之中
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();//如果和SUCEESS中的值，也就是0相等则返回true
    }
    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        //因为上面使用的是T，所以就算T为String，也会调用private ServerResponse(int status,String msg,T data)，
        //否则没有这个方法，当传来的是String类型的时候，调用的为 private ServerResponse(int status,String msg)
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(){//创建成功的提示
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());//会调用 private ServerResponse(int status)
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByError(){//创建错误的提示
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDEsc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){//错误提示
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){//一些错误提示，例如NEED_LOGIN
        return new ServerResponse<T>(errorCode,errorMessage);
    }
}















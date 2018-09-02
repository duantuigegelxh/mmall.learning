package com.mmall.common;

/**
 * Created by 刘湘海 on 2018/7/29.
 */
//这是一个常量类，很多常量可以在这里取
public class Const {

    public static final String CURRENT_USER="currentUser";//最近登录用户标识

    public static final String EMAIL="email";

    public static final String USERNAME="username";

    //通过一个接口类，将我们所需要的常量进行分组,变量必须是public、static、final的，方法必须是public、abstract的
    public interface Role(){
        int ROLE_CUSTOMER=0;//普通用户
        int ROLE_ADMIN=1;//管理员
    }
}

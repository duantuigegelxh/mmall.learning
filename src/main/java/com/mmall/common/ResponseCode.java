package com.mmall.common;

/**
 */
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),//枚举,还没有写枚举的构造器，需要被使用才不会报错，这里在ServerResponse中使用了,
    ERROR(1,"ERROR"),//枚举之间用逗号隔开
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;;

    ResponseCode(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public int getCode(){
        return code;
    }
    public String getDEsc(){
        return desc;
    }

}

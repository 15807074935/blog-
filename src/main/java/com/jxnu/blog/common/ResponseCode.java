package com.jxnu.blog.common;

public enum ResponseCode {
    SUCCESS(1,"SUCCESS"),
    ERROR(0,"ERROR");
    private final int code;
    private final String msg;
    ResponseCode(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

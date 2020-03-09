package com.jxnu.blog.common;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {
    private int code;
    private T data;
    private String msg;
    private ServerResponse(int code, T data, String msg){
        this.code=code;
        this.data=data;
        this.msg=msg;
    }
    private ServerResponse(int code, T data){
        this.code=code;
        this.data=data;
    }
    public static <T> ServerResponse<T> createBySuccess(T data, String msg){
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),data,msg);
    }
    public static <T> ServerResponse<T> createByError(T data, String msg){
        return new ServerResponse<>(ResponseCode.ERROR.getCode(),data,msg);
    }
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse<T> createByError(T data){
        return new ServerResponse<>(ResponseCode.ERROR.getCode(),data);
    }
    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
    public boolean isSuccess(){
        return this.getCode()==ResponseCode.SUCCESS.getCode();
    }
}

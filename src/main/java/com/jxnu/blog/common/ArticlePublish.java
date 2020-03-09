package com.jxnu.blog.common;

public enum ArticlePublish {
    ISPUBLISH(1,"PUBLISH"),
    NOTPUBLISH(0,"NOTPUBLISH");
    int code;
    String msg;
    ArticlePublish(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

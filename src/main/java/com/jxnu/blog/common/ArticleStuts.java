package com.jxnu.blog.common;

public enum ArticleStuts {
    online(1,"ONLINE"),
    down(0,"DOWN");
    int code;
    String msg;
    ArticleStuts(int code,String msg){
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

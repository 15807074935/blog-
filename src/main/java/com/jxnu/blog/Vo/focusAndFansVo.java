package com.jxnu.blog.Vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
//@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class focusAndFansVo {
    int focuss;
    int fans;

    public int getFocuss() {
        return focuss;
    }

    public void setFocuss(int focuss) {
        this.focuss = focuss;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }
}

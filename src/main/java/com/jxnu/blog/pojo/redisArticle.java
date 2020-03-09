package com.jxnu.blog.pojo;

public class redisArticle implements Comparable{
    private String key;
    private int view;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    @Override
    public int compareTo(Object o) {
        return ((redisArticle)o).view-this.view;
    }
}

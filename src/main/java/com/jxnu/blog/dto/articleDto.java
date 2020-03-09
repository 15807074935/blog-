package com.jxnu.blog.dto;

public class articleDto {
    private String detail;
    private String withcode;
    private String title;
    private int id;
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getWithcode() {
        return withcode;
    }

    public void setWithcode(String withcode) {
        this.withcode = withcode;
    }
}

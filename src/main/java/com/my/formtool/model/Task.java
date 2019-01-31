package com.my.formtool.model;

import java.util.Date;

public class Task {
    private Integer id;

    private String name;

    private Boolean isopen;

    private Boolean isunique;

    private Date addtime;

    private Integer author;

    private Boolean status;

    private String theme;

    private String fields;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getIsopen() {
        return isopen;
    }

    public void setIsopen(Boolean isopen) {
        this.isopen = isopen;
    }

    public Boolean getIsunique() {
        return isunique;
    }

    public void setIsunique(Boolean isunique) {
        this.isunique = isunique;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme == null ? null : theme.trim();
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields == null ? null : fields.trim();
    }
}
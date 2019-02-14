package com.my.formtool.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class Task {
    private Integer id;

    private String name;

    private Boolean isopen = false;

    private Boolean isunique = false;

    private Timestamp addtime = new Timestamp(System.currentTimeMillis());;

    private Integer author;

    private Integer status = 1;

    private String theme;

    private String fields = null;

    private  Integer fieldCount = 0;

    private JSONArray fieldList = null;

    private Integer formCount = 0;

    private  Integer userCount = 0;

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

    public Timestamp getAddtime() {
        return addtime;
    }

    public void setAddtime(Timestamp addtime) {
        this.addtime = addtime;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public Integer getFieldCount(){return fieldCount;}

    public void setFieldCount(Integer fieldCount) {
        this.fieldCount = fieldCount;
    }

    public JSONArray getFieldList() {
        return fieldList;
    }

    public void setFieldList(JSONArray fieldList) {
        this.fieldList = fieldList;
    }

    public void setFormCount(Integer formCount) {
        this.formCount = formCount;
    }

    public Integer getFormCount() {
        return formCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getUserCount() {
        return userCount;
    }
}
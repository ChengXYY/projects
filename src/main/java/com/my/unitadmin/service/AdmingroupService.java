package com.my.unitadmin.service;

import com.my.unitadmin.model.Admingroup;

import java.util.List;
import java.util.Map;

public interface AdmingroupService {
    //list all
    List<Admingroup> getListAll();

    //add
    int add(Admingroup admingroup);

    //edit
    int edit(Map<String, Object> admingroup);

    //delete
    int remove(Integer id);

    //get
    Admingroup get(Integer id);

    //auth
    void changeAuth(Integer id, String[] auths);

}

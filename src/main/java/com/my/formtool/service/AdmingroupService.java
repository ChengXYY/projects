package com.my.formtool.service;

import com.my.formtool.model.Admingroup;

import java.util.List;

public interface AdmingroupService {
    //list all
    List<Admingroup> getListAll();

    //add
    int add(Admingroup admingroup);

    //edit
    int edit(Admingroup admingroup);

    //delete
    int remove(Integer id);

    //get
    Admingroup get(Integer id);

}

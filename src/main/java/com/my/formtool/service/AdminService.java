package com.my.formtool.service;

import com.my.formtool.model.Admin;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

public interface AdminService {
    //add user
    int add(Admin admin);
    //update user
    int edit(Admin admin);
    //delete user
    int remove(Integer id);

    Admin get(Integer id);

    List<Admin> getList(Map<String, Object> filter);

    int resetPassword(Integer id);
}

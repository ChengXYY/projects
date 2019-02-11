package com.my.formtool.service;

import com.my.formtool.model.Admin;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface AdminService {
    //add user
    int add(Admin admin);
    //update user
    int edit(Map<String, Object> admin);
    //delete user
    int remove(Integer id);

    Admin get(Integer id);

    Admin get(String account);

    List<Admin> getList(Map<String, Object> filter);

    int resetPassword(Integer id);

    void login(String account, String password, String vercode, HttpSession session);

    void editPassword(String oldpwd, String newpwd, String repwd, HttpSession session);

    Admin getCurrentUser();
}

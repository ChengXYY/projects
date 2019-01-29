package com.my.formtool.service;

import java.util.List;
import java.util.Map;

public interface AdminService {
    //add user
    int add(Map<String, Object> admin);
    //update user
    int edit(Map<String, Object> admin);
    //delete user
    int remove(Integer id);

    Map<String, Object> get(Integer id);

    List<Map<String, Object>> getList(Map<String, Object> filter);
}

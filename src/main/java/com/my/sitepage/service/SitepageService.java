package com.my.sitepage.service;

import com.my.sitepage.model.Sitepage;

import java.util.List;
import java.util.Map;

public interface SitepageService {
    //add
    int add(Sitepage page);
    //edit
    int edit(Map<String, Object> page);
    //delete
    int remove(Integer id);
    //get one
    Sitepage get(Integer id);
    //get list
    List<Sitepage> getList(Map<String, Object> filter);
    //get count
    Integer getCount(Map<String, Object> filter);
}

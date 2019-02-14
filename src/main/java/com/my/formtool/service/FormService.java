package com.my.formtool.service;

import com.alibaba.fastjson.JSONObject;
import com.my.formtool.model.Form;

import java.util.List;
import java.util.Map;

public interface FormService {
    //submit
    int submit(Map<String, Object> data);

    //get list
    List<Form> getList(Map<String, Object> filter);

    //get count
    Integer getCount(Map<String, Object> filter);
}

package com.my.formtool.service;

import com.alibaba.fastjson.JSONObject;
import com.my.formtool.model.Form;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FormService {
    //submit
    int submit(Map<String, Object> data);

    //get list
    List<Form> getList(Map<String, Object> filter);

    //get count
    Integer getCount(Map<String, Object> filter);

    //获取图表数据
    JSONObject chartData(Integer taskid);

    //获取表个数
    Integer getChartCount(Integer taskid);

    //get one
    Form get(Integer id);

    //remove
    int remove(Integer id);

    //remove batch
    int removeBatch(Integer taskid);

}

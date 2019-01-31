package com.my.formtool.mapper;

import com.my.formtool.model.Form;

import java.util.List;
import java.util.Map;

public interface FormMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Form record);

    Map<String, Object> selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map<String, Object> record);

    List<Map<String, Object>> selectByFilter(Map<String, Object> filter);
}
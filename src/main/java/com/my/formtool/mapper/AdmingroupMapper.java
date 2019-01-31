package com.my.formtool.mapper;

import com.my.formtool.model.Admingroup;

import java.util.List;
import java.util.Map;

public interface AdmingroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Admingroup record);

    int insertSelective(Admingroup record);

    Admingroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admingroup record);

    int updateByPrimaryKey(Admingroup record);

    List<Admingroup> selectAll();
}
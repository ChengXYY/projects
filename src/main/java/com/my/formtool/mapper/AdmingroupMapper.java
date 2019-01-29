package com.my.formtool.mapper;

import com.my.formtool.model.Admingroup;

public interface AdmingroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Admingroup record);

    int insertSelective(Admingroup record);

    Admingroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admingroup record);

    int updateByPrimaryKey(Admingroup record);
}
package com.my.formtool.mapper;


import com.my.formtool.model.Admin;

import java.util.List;
import java.util.Map;

public interface AdminMapper {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Admin admin);

    Admin selectByPrimaryKey(Integer id);

    List<Admin> selectByFilter(Map<String, Object> filter);

    int updateByPrimaryKeySelective(Admin admin);
}
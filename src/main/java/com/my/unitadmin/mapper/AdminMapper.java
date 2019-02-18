package com.my.unitadmin.mapper;


import com.my.formtool.model.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

public interface AdminMapper {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Admin admin);

    int insert(Admin admin);

    Admin selectByPrimaryKey(Integer id);

    Admin selectByAccount(String account);

    List<Admin> selectByFilter(Map<String, Object> filter);

    int updateByPrimaryKeySelective(Map<String, Object> admin);
}
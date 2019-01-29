package com.my.formtool.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

public interface AdminMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Map<String, Object> admin);

    Map<String, Object> selectByPrimaryKey(Integer id);

    List<Map<String, Object>> selectByFilter(Map<String, Object> filter);

    int updateByPrimaryKeySelective(Map<String, Object> admin);
}
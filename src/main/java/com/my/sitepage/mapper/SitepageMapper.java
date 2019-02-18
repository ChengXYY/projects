package com.my.sitepage.mapper;

import com.my.sitepage.model.Sitepage;

import java.util.List;
import java.util.Map;

public interface SitepageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sitepage record);

    int insertSelective(Sitepage record);

    Sitepage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map<String, Object> record);

    Integer countByFilter(Map<String, Object> filter);

    List<Sitepage> selectByFilter(Map<String, Object> filter);
}
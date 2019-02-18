package com.my.filemanager.mapper;

import com.my.filemanager.model.Filemanager;

import java.util.List;
import java.util.Map;

public interface FilemanagerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Filemanager record);

    int insertSelective(Filemanager record);

    Filemanager selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map<String, Object> filter);

    List<Filemanager> selectByFilter(Map<String, Object> filter);

    Integer countByFilter(Map<String, Object> filter);
}
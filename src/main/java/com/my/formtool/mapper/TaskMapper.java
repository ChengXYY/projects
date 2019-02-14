package com.my.formtool.mapper;

import com.my.formtool.model.Task;

import java.util.List;
import java.util.Map;

public interface TaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map<String, Object> record);

    List<Task> selectByFilter(Map<String, Object> filter);

    int countByFilter(Map<String, Object> filter);

}
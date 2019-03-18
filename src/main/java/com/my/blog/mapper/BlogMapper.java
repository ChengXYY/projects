package com.my.blog.mapper;

import com.my.blog.model.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map<String, Object> record);

    List<Blog> selectByFilter(Map<String, Object> filter);

    int countByFilter(Map<String, Object> filter);

}
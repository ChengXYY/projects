package com.my.email.mapper;

import com.my.email.model.Email;

import java.util.List;
import java.util.Map;

public interface EmailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Email record);

    int insertSelective(Email record);

    Email selectByPrimaryKey(Integer id);

    Email selectByCode(String code);

    int updateByPrimaryKeySelective(Email record);

    int updateByPrimaryKey(Email record);

    List<Email> selectByFilter(Map<String ,Object>filter);

    int countByFilter(Map<String, Object>filter);
}
package com.my.email.mapper;

import com.my.email.model.EmailRead;

public interface EmailReadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmailRead record);

    int insertSelective(EmailRead record);

    EmailRead selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmailRead record);

    int updateByPrimaryKey(EmailRead record);
}
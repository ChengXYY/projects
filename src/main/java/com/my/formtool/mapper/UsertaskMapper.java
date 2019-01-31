package com.my.formtool.mapper;

import com.my.formtool.model.Usertask;

public interface UsertaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Usertask record);

    int insertSelective(Usertask record);

    Usertask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Usertask record);

    int updateByPrimaryKey(Usertask record);
}
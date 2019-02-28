package com.my.unitadmin.mapper;

import com.my.unitadmin.model.Admingroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdmingroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Admingroup record);

    int insertSelective(Admingroup record);

    Admingroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map<String, Object> record);

    int updateByPrimaryKey(Admingroup record);

    List<Admingroup> selectAll(@Param("parentid") Integer parentid);
}
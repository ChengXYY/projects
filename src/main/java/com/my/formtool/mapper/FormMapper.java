package com.my.formtool.mapper;

import com.my.formtool.model.Form;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FormMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Form record);

    Form selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map<String, Object> record);

    List<Form> selectByFilter(Map<String, Object> filter);

    Integer countByFilter(Map<String, Object> filter);

    List<String> getForms(@Param("taskid") Integer taskid);

    int deleteByTaskid(@Param("taskid") Integer taskid);
}
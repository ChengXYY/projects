package com.my.formtool.service.serviceimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.my.formtool.common.CommonOperation;
import com.my.formtool.exception.JsonException;
import com.my.formtool.mapper.TaskMapper;
import com.my.formtool.model.Task;
import com.my.formtool.model.result.ErrorCodes;
import com.my.formtool.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public int add(Task task) {
        if(task.getName().isEmpty() || task.getFields().isEmpty())throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        return taskMapper.insertSelective(task);
    }

    @Override
    public int edit(Map<String, Object> task) {
        if(task.get("id")== null || !CommonOperation.checkId(Integer.parseInt(task.get("id").toString())))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        return taskMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    public int remove(Integer id) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        return taskMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Task> getList(Map<String, Object> filter) {
        List<Task> taskList =  taskMapper.selectByFilter(filter);
        for (int i=0; i<taskList.size(); i++){
            //实现fieldCount 字段数
            if(taskList.get(i).getFields() != null){
                JSONArray fieldList = JSONArray.parseArray(taskList.get(i).getFields());
                taskList.get(i).setFieldList(fieldList);
                taskList.get(i).setFieldCount(fieldList.size());
            }

            //实现formCount 表单提交数
            //实现userCount 邀请用户数
        }
        return taskList;
    }

    @Override
    public int getCount(Map<String, Object> filter) {
        return taskMapper.countByFilter(filter);
    }

    @Override
    public Task get(Integer id) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Task task = taskMapper.selectByPrimaryKey(id);
        if(task == null)throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        //实现 fieldCount
        JSONArray fieldList = JSONArray.parseArray(task.getFields());
        task.setFieldCount(fieldList.size());
        task.setFieldList(fieldList);
        //实现formCount 表单提交数
        //实现userCount 邀请用户数
        return task;
    }
}

package com.my.formtool.service;

import com.my.formtool.model.Task;

import java.util.List;
import java.util.Map;

public interface TaskService {

    //add
    int add(Task task);

    //edit
    int edit(Map<String, Object> task);

    //remove
    int remove(Integer id);

    //get list
    List<Task> getList(Map<String, Object> filter);

    //get count
    int getCount(Map<String, Object>filter);

    //get one
    Task get(Integer id);

}

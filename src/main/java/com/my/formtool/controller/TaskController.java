package com.my.formtool.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.my.formtool.config.Permission;
import com.my.formtool.exception.JsonException;
import com.my.formtool.model.Task;
import com.my.formtool.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/task")
@Permission("1003")
public class TaskController {

    @Autowired
    private TaskService taskService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${formtool.weburl}")
    private String formWebUrl;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String taskList(@RequestParam(value = "page", required = false)Integer page,
                           @RequestParam(value = "keyword" ,required = false)String keyword,
                           @RequestParam(value = "isopen", required = false)Integer isopen, ModelMap model){
        logger.info(keyword);
        Map<String, Object> filter = new HashMap<String, Object>();
       if(keyword!=null && !keyword.isEmpty()){
           filter.put("name", keyword);
       }
       if(isopen!=null && (isopen == 0 || isopen == 1)){
           filter.put("isopen", isopen);
       }
        List<Task> taskList = taskService.getList(filter);
        Integer taskCount = taskService.getCount(filter);

        model.addAttribute("list", taskList);
        model.addAttribute("taskCount", taskCount);
        model.addAttribute("name",keyword);
        model.addAttribute("isopen", isopen);
        model.addAttribute("FORM_WEB", formWebUrl);
        model.addAttribute("pageTitle","表单任务列表 - 表单提交平台 - 后台管理系统");

        model.addAttribute("TopMenuFlag", "formtool");
        return "admin/task_list";
    }

    @RequestMapping("/add")
    public String taskAdd(ModelMap model){
        model.addAttribute("pageTitle","添加表单任务 - 表单提交平台 - 后台管理系统");

        model.addAttribute("TopMenuFlag", "formtool");
        return "admin/task_add";
    }

    @ResponseBody
    @RequestMapping(value = "/add/submit", method = RequestMethod.POST)
    public JSONObject taskAdd(Task task){
        JSONObject result = new JSONObject();
        try {
            taskService.add(task);
            result.put("code", 1);
            result.put("msg", "添加成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String taskEdit(@PathVariable(value = "id",required = true)Integer id, ModelMap model){
        try {
            Task task = taskService.get(id);
            model.addAttribute("task", task);
            model.addAttribute("TopMenuFlag", "formtool");
            model.addAttribute("pageTitle","编辑表单任务 - 表单提交平台 - 后台管理系统");
            return "/admin/task_edit";
        }catch (JSONException e){
            model.addAttribute("pageTitle","404 Error");
            return "error/404";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit/submit", method = RequestMethod.POST)
    public JSONObject taskEdit(Task task){
        Map<String, Object>taskMap = new HashMap<>();
        taskMap.put("id", task.getId());
        taskMap.put("name", task.getName());
        taskMap.put("status", task.getStatus());
        taskMap.put("isopen", task.getIsopen());
        taskMap.put("isunique", task.getIsunique());
        taskMap.put("theme", task.getTheme());
        taskMap.put("fields", task.getFields());
        JSONObject result = new JSONObject();
        try {
            taskService.edit(taskMap);
            result.put("code", 1);
            result.put("msg", "修改成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }
}

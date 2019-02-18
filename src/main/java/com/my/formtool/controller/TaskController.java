package com.my.formtool.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.my.common.aop.Permission;
import com.my.common.exception.JsonException;
import com.my.formtool.model.Task;
import com.my.formtool.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Value("${list.pagesize}")
    private Integer pageSize;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String taskList(@RequestParam(value = "page", required = false)Integer page,
                           @RequestParam(value = "keyword" ,required = false)String keyword,
                           @RequestParam(value = "isopen", required = false)Integer isopen,
                           HttpServletRequest request, ModelMap model){
        try{
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("order", "id desc");
            String currentUrl = request.getRequestURI();
            if(keyword!=null && !keyword.isEmpty()){
                filter.put("name", keyword);
                currentUrl += "?keyword="+keyword;
            }
            if(isopen!=null && (isopen == 0 || isopen == 1)){
                filter.put("isopen", isopen);
                currentUrl += (currentUrl.contains("?")?"&":"?")+"isopen"+isopen;
            }
            if(page == null || page<1){
                page = 1;
            }
            int totalCount = taskService.getCount(filter);
            int pageCount = (int)Math.ceil(totalCount/pageSize);
            if(pageCount <1){
                pageCount = 1;
            }

            filter.put("page", (page-1)*pageSize);
            filter.put("pagesize", pageSize);

            List<Task> taskList = taskService.getList(filter);

            model.addAttribute("list", taskList);
            model.addAttribute("name",keyword);
            model.addAttribute("isopen", isopen);
            model.addAttribute("FORM_WEB", formWebUrl);

            model.addAttribute("currentPage", page);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("currentUrl", currentUrl);
            model.addAttribute("pageTitle","表单任务列表 - 表单提交平台 - 后台管理系统");

            model.addAttribute("TopMenuFlag", "formtool");
            return "formtool/task_list";
        }catch (JSONException e){
            model.addAttribute("error", e);
            return "error/500";
        }
    }

    @RequestMapping("/add")
    public String taskAdd(ModelMap model){
        model.addAttribute("pageTitle","添加表单任务 - 表单提交平台 - 后台管理系统");

        model.addAttribute("TopMenuFlag", "formtool");
        return "formtool/task_add";
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
            return "formtool/task_edit";
        }catch (JSONException e){
            model.addAttribute("error", e);
            return "error/404";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit/submit", method = RequestMethod.POST)
    public JSONObject taskEdit(@RequestParam Map<String, Object> task){
        JSONObject result = new JSONObject();
        try {
            taskService.edit(task);
            result.put("code", 1);
            result.put("msg", "修改成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JSONObject taskRemove(@RequestParam(value = "id", required = true)Integer id){
        JSONObject result = new JSONObject();
        try {
            taskService.remove(id);
            result.put("code", 1);
            result.put("msg", "关闭成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public JSONObject taskClose(@RequestParam(value = "id", required = true)Integer id){
        JSONObject result = new JSONObject();
        try {
            taskService.close(id);
            result.put("code", 1);
            result.put("msg", "重启成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/reboot", method = RequestMethod.POST)
    public JSONObject taskReboot(@RequestParam(value = "id", required = true)Integer id){
        JSONObject result = new JSONObject();
        try {
            taskService.reboot(id);
            result.put("code", 1);
            result.put("msg", "删除成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }
}

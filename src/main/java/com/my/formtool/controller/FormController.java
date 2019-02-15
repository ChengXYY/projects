package com.my.formtool.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.common.exception.JsonException;
import com.my.formtool.model.Task;
import com.my.formtool.service.FormService;
import com.my.formtool.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/form")
public class FormController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/{id}")
    public String index(@PathVariable(value = "id", required = true)Integer id, ModelMap model){
        try {
            Task task = taskService.get(id);
            model.addAttribute("task", task);
            model.addAttribute("THEME", task.getTheme());logger.info(task.getTheme());
            model.addAttribute("userForms",null);
            return "/formtool/index";
        }catch (JsonException e){
            return "/error/404";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public JSONObject sumit(@RequestParam Map<String, Object> data){

        JSONObject result = new JSONObject();
        try {
            formService.submit(data);
            result.put("code", 1);
            result.put("msg", "提交成功");
            return result;
        }catch (JsonException e) {
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }
}

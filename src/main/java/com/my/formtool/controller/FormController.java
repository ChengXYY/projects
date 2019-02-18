package com.my.formtool.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.common.aop.Permission;
import com.my.common.exception.JsonException;
import com.my.formtool.model.Form;
import com.my.formtool.model.Task;
import com.my.formtool.service.FormService;
import com.my.formtool.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/form")
public class FormController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Value("${list.pagesize}")
    private Integer pageSize;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/{id}")
    public String index(@PathVariable(value = "id", required = true)Integer id, ModelMap model){
        try {
            Task task = taskService.get(id);
            model.addAttribute("task", task);
            model.addAttribute("THEME", task.getTheme());logger.info(task.getTheme());
            model.addAttribute("userForms",null);
            return "formtool/index";
        }catch (JsonException e){
            return "error/404";
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

    @Permission("1003")
    @RequestMapping("/list/{id}")
    public String list(@PathVariable(value = "id", required = true)Integer id,
                       @RequestParam(value = "page", required = false)Integer page,
                       HttpServletRequest request,
                       ModelMap model){
        try {
            String currentUrl = request.getRequestURI();

            Task task = taskService.get(id);
            Map<String, Object>filter = new HashMap<>();
            filter.put("taskid", id);
            int totalCount = formService.getCount(filter);
            if(page == null || page<1){
                page = 1;
            }
            int pageCount = (int)Math.ceil(totalCount/pageSize);
            if(pageCount <1) {
                pageCount = 1;
            }

            filter.put("page", (page-1)*pageSize);
            filter.put("pagesize", pageSize);

            List<Form> formList = formService.getList(filter);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("currentUrl", currentUrl);

            model.addAttribute("task", task);
            model.addAttribute("list", formList);

            model.addAttribute("pageTitle","表单列表 - 表单提交平台 - 后台管理系统");
            model.addAttribute("TopMenuFlag", "system");

            return "formtool/form_list";
        }catch (JsonException e){
            String returnUrl = "error/";
            switch (e.getCode()){
                case ID_NOT_ILLEGAL:
                    returnUrl += "404";
                    break;
                default:
                    returnUrl += "500";
                    break;
            }
            model.addAttribute("error", e);
            return returnUrl;
        }
    }
}

package com.my.formtool.controller;

import com.my.formtool.service.AdminService;
import com.my.formtool.service.AdmingroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdmingroupService admingroupService;

    @RequestMapping("/index")
    public String index(ModelMap model){
        model.addAttribute("pageTitle","首页 - 后台管理系统");
        model.addAttribute("TopMenuFlag", "index");
        return "admin/index";
    }

    @RequestMapping("/adminlist")
    public String adminList(@RequestParam(value = "page", required = false, defaultValue = "0")String page,
                            ModelMap model){
        Map<String, Object>filter = new HashMap<String, Object>();

        int pageint = Integer.parseInt(page);
        int pagesizeint = 15;
        if(pageint>0){
            pageint = (pageint-1)*pagesizeint;
            filter.put("page",pageint);
            filter.put("pagesize",pagesizeint);
        }
        List<Map<String, Object>> list = adminService.getList(filter);

        model.addAttribute("list", list);
        model.addAttribute("pageTitle","管理员列表 - 系统设置 - 后台管理系统");

        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "admin");
        return "admin/admin_list";
    }

    @RequestMapping("/resetpwd")
    public String passwordReset(){
        return "admin/resetpwd";
    }

    @ResponseBody
    @RequestMapping(value = "/resetsubmit", produces = {"application/json;charset=UTF-8"})
    public Map<String, Object> passwordReset(Map<String, Object> myform){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 1);
        result.put("msg", "添加成功");
        return result;
    }

    @RequestMapping("/adminadd")
    public String adminAdd(ModelMap model){
        List<Map<String, Object>> list = admingroupService.getListAll();
        model.addAttribute("list", list);
        return "admin/admin_add";
    }

    // AdminGroup 处理
    @RequestMapping("/admingroup")
    public String admingroupList(ModelMap model){
        List<Map<String, Object>> list = admingroupService.getListAll();
        model.addAttribute("list", list);
        model.addAttribute("pageTitle","管理员列表 - 系统设置 - 后台管理系统");

        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "admingroup");
        return "admin/admingroup_list";
    }
}

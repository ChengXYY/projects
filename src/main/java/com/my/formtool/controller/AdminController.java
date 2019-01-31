package com.my.formtool.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.formtool.exception.JsonException;
import com.my.formtool.model.Admin;
import com.my.formtool.model.Admingroup;
import com.my.formtool.service.AdminService;
import com.my.formtool.service.AdmingroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/list")
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
        List<Admin> list = adminService.getList(filter);

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
    @RequestMapping(value = "/resetpwd/submit", produces = {"application/json;charset=UTF-8"})
    public JSONObject passwordReset(Integer id){

        JSONObject result = new JSONObject();
        try{
            adminService.resetPassword(id);
            result.put("code", 1);
            result.put("msg", "添加成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }

    @RequestMapping("/add")
    public String adminAdd(ModelMap model){
        List<Admingroup> list = admingroupService.getListAll();
        model.addAttribute("list", list);
        return "admin/admin_add";
    }

    @ResponseBody
    @RequestMapping(value = "/add/submit", produces = {"application/json;charset=UTF-8"})
    public JSONObject adminAdd(Admin admin){

        JSONObject result = new JSONObject();

        try {
            adminService.add(admin);
            result.put("code", 1);
            result.put("msg", "添加成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }

    @RequestMapping("/edit/{id}")
    public String adminEdit(@PathVariable("id") Integer id, ModelMap model){
        Admin admin = adminService.get(id);
        if(admin == null){
            model.addAttribute("pageTitle","404 Error");
            return "error/404";
        }
        List<Admingroup> list = admingroupService.getListAll();
        model.addAttribute("list", list);
        model.addAttribute("admin", admin);
        model.addAttribute("pageTitle","编辑管理员信息");
        return "admin/admin_edit";
    }
}

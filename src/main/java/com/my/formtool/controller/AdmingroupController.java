package com.my.formtool.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.formtool.model.Admingroup;
import com.my.formtool.model.result.AuthCode;
import com.my.formtool.service.AdmingroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.formtool.exception.JsonException;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admingroup")
public class AdmingroupController {

    @Autowired
    private AdmingroupService admingroupService;

    // AdminGroup 处理
    @RequestMapping("/list")
    public String admingroupList(ModelMap model){
        List<Admingroup> list = admingroupService.getListAll();
        model.addAttribute("list", list);
        model.addAttribute("pageTitle","管理员列表 - 系统设置 - 后台管理系统");

        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "admingroup");
        return "admin/admingroup_list";
    }

    @RequestMapping("/add")
    public String admingroupAdd(ModelMap model){
        return "admin/admingroup_add";
    }

    @ResponseBody
    @RequestMapping("/add/submit")
    public JSONObject admingroupAdd(Admingroup admingroup){
        JSONObject result = new JSONObject();
        try {
            admingroupService.add(admingroup);
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
    public String admingroupEdit(@PathVariable("id") Integer id, ModelMap model){

        Admingroup admingroup = admingroupService.get(id);
        model.addAttribute("item", admingroup);
        return "admin/admingroup_edit";
    }

    //权限
    @RequestMapping("/auth/{id}")
    public String admingrouAuth(@PathVariable("id")Integer id, ModelMap model){
        try {
            Admingroup admingroup = admingroupService.get(id);
            String authStr = admingroup.getAuth();
            model.addAttribute("authlist", authStr);
            List<Map<String, Object>> list = AuthCode.listAuthCode();
            model.addAttribute("list", list);
            model.addAttribute("groupid", id);
            model.addAttribute("pageTitle","管理员组权限配置 - 系统设置 - 后台管理系统");

            model.addAttribute("TopMenuFlag", "system");
            model.addAttribute("LeftMenuFlag", "admingroup");
            return "admin/admingroup_auth";
        }catch (JsonException e){
            return "error/404";
        }

    }
}

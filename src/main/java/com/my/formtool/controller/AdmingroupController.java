package com.my.formtool.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.formtool.config.RequiredPermission;
import com.my.formtool.model.Admingroup;
import com.my.formtool.model.result.AuthCode;
import com.my.formtool.service.AdmingroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.my.formtool.exception.JsonException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admingroup")
@RequiredPermission("1001")
public class AdmingroupController {

    @Autowired
    private AdmingroupService admingroupService;
    private Logger logger = LoggerFactory.getLogger(getClass());

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

    @ResponseBody
    @RequestMapping("/edit/submit")
    public JSONObject admingroupEdit(Admingroup admingroup){
        Map<String, Object> group = new HashMap<>();
        group.put("id", admingroup.getId());
        group.put("sort", admingroup.getSort());
        group.put("name", admingroup.getName());
        JSONObject result = new JSONObject();
        try {
            admingroupService.edit(group);
            result.put("code", 1);
            result.put("msg", "修改成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
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

    @ResponseBody
    @RequestMapping(value = "/auth/save", method = RequestMethod.POST)
    public JSONObject authSave(Integer id, @RequestParam(value = "authcodes[]") String[] authcodes){
        JSONObject result = new JSONObject();
        try {
            admingroupService.changeAuth(id, authcodes);
            result.put("code", 1);
            result.put("msg", "保存成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }
}

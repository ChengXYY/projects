package com.my.formtool.controller;

import com.my.formtool.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/index")
    public String index(ModelMap model){
        model.addAttribute("pageTitle","首页 - 后台管理系统");
        model.addAttribute("TopMenuFlag", "index");
        return "admin/index";
    }

    @RequestMapping("/adminlist")
    public String adminList(@RequestParam(value = "name",required = false, defaultValue = "")String name,
                            @RequestParam(value ="groupid",required = false, defaultValue = "0")String groupid,
                            @RequestParam(value = "order", required = false, defaultValue = "id")String order,
                            @RequestParam(value = "page", required = false, defaultValue = "0")String page,
                            @RequestParam(value = "pagesize",required = false, defaultValue = "0")String pagesize,
                            ModelMap model){
        Map<String, Object>filter = new HashMap<String, Object>();
        if(!name.isEmpty()){
            filter.put("name",name);
        }
        int groupidint = Integer.parseInt(groupid);
        if(groupidint >0){
            filter.put("groupid",groupid);
        }
        if(!order.isEmpty()){
            filter.put("order", order);
        }
        int pageint = Integer.parseInt(page);
        int pagesizeint = Integer.parseInt(pagesize);
        if(pageint>0 && pagesizeint>0){
            pageint = (pageint-1)*pagesizeint;
            filter.put("page",pageint);
            filter.put("pagesize",pagesizeint);
        }
        List<Map<String, Object>> list = adminService.getList(filter);

        model.addAttribute("list", list);
        model.addAttribute("pageTitle","管理员列表 - 系统设置 - 后台管理系统");

        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "admin");
        return "admin/adminlist";
    }

    @RequestMapping(value = "/adminadd", produces = {"application/json;charset=UTF-8"})
    public Map<String, Object> adminAdd(Map<String, Object> admin){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 1);
        result.put("msg", "添加成功");
        return result;
    }
}

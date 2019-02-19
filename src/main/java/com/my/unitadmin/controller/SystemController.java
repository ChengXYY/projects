package com.my.unitadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.common.aop.Permission;
import com.my.common.exception.JsonException;
import com.my.unitadmin.model.Admin;
import com.my.unitadmin.model.Admingroup;
import com.my.unitadmin.model.Adminlog;
import com.my.common.result.AuthCode;
import com.my.unitadmin.service.AdminService;
import com.my.unitadmin.service.AdmingroupService;
import com.my.unitadmin.service.AdminlogService;
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
@RequestMapping("/system")
@Permission("1001")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdmingroupService admingroupService;
    @Autowired
    private AdminlogService adminlogService;

    @Value("${list.pagesize}")
    private Integer pageSize;

    @RequestMapping("/admin/list")
    public String adminList(ModelMap model){
        Map<String, Object>filter = new HashMap<String, Object>();

        filter.put("order", "id asc");
        List<Admin> list = adminService.getList(filter);

        int totalCount = list.size();
        model.addAttribute("list", list);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pageTitle","管理员列表 - 系统设置 - 后台管理系统");

        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "admin");
        return "admin/admin_list";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/resetpwd/submit", produces = {"application/json;charset=UTF-8"})
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

    @RequestMapping("/admin/add")
    public String adminAdd(ModelMap model){
        List<Admingroup> list = admingroupService.getListAll();
        model.addAttribute("list", list);
        return "admin/admin_add";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/add/submit", produces = {"application/json;charset=UTF-8"})
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
    @RequestMapping("/admin/edit/{id}")
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

    @ResponseBody
    @RequestMapping(value = "/admin/edit/submit", method = RequestMethod.POST)
    public JSONObject editAdmin(@RequestParam Map<String, Object> admin ){
        JSONObject result = new JSONObject();

        try {
            adminService.edit(admin);
            result.put("code", 1);
            result.put("msg", "保存成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/admin/remove", method = RequestMethod.POST)
    public JSONObject removeAdmin(@RequestParam(value = "id", required = true)Integer id){

        JSONObject result = new JSONObject();

        try {
            adminService.remove(id);
            result.put("code", 1);
            result.put("msg", "删除成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }

    // AdminGroup 处理
    @RequestMapping("/admingroup/list")
    public String admingroupList(ModelMap model){
        List<Admingroup> list = admingroupService.getListAll();
        model.addAttribute("list", list);

        int totalCount = list.size();
        model.addAttribute("pageTitle","管理员列表 - 系统设置 - 后台管理系统");

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "admingroup");
        return "admin/admingroup_list";
    }

    @RequestMapping("/admingroup/add")
    public String admingroupAdd(ModelMap model){
        return "admin/admingroup_add";
    }

    @ResponseBody
    @RequestMapping("/admingroup/add/submit")
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

    @RequestMapping("/admingroup/edit/{id}")
    public String admingroupEdit(@PathVariable("id") Integer id, ModelMap model){

        Admingroup admingroup = admingroupService.get(id);
        model.addAttribute("item", admingroup);
        return "admin/admingroup_edit";
    }

    @ResponseBody
    @RequestMapping("/admingroup/edit/submit")
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
    @RequestMapping("/admingroup/auth/{id}")
    public String admingrouAuth(@PathVariable("id")Integer id, ModelMap model){
        try {
            Admingroup admingroup = admingroupService.get(id);
            String authStr = admingroup.getAuth();
            model.addAttribute("authlist", authStr);
            List<Map<String, Object>> list = AuthCode.listAuthCode();
            model.addAttribute("list", list);
            model.addAttribute("groupid", id);
            model.addAttribute("groupName", admingroup.getName());
            model.addAttribute("pageTitle","管理员组权限配置 - 系统设置 - 后台管理系统");

            model.addAttribute("TopMenuFlag", "system");
            model.addAttribute("LeftMenuFlag", "admingroup");
            return "admin/admingroup_auth";
        }catch (JsonException e){
            return "error/404";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/admingroup/auth/save", method = RequestMethod.POST)
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

    //log list
    @RequestMapping(value = "/adminlog/list", method = RequestMethod.GET)
    public String adminlogList(@RequestParam(value = "content", required = false)String content,
                                @RequestParam(value = "page", defaultValue = "1", required = false)Integer page,
                               HttpServletRequest request,
                               ModelMap model){
        Map<String, Object>filter = new HashMap<>();
        filter.put("order", "id desc");
        String currentUrl = request.getRequestURI();
        if(content!=null && !content.isEmpty()){
            filter.put("content",content);
            currentUrl += "?content="+content;
        }else{
            content = "";
        }
        if(page == null || page<1){
            page = 1;
        }
        int totalCount = adminlogService.getCount(filter);
        int pageCount = (int)Math.ceil(totalCount/pageSize);
        if(pageCount <1){
            pageCount = 1;
        }

        filter.put("page", (page-1)*pageSize);
        filter.put("pagesize", pageSize);
        List<Adminlog> list = adminlogService.getList(filter);

        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentUrl", currentUrl);

        model.addAttribute("list", list);
        model.addAttribute("content", content);

        model.addAttribute("pageTitle","管理员日志 - 系统设置 - 后台管理系统");
        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "adminlog");
        return "admin/adminlog_list";
    }
}

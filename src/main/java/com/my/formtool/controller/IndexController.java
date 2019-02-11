package com.my.formtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller //@RestController 默认返回json  @Controller 返回值需配合 @ResponseBody
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("/admin")
    public String index(ModelMap model){
        model.addAttribute("pageTitle","首页 - 后台管理系统");
        model.addAttribute("TopMenuFlag", "index");
        return "admin/index";
    }


}

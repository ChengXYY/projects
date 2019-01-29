package com.my.formtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller //@RestController 默认返回json  @Controller 返回值需配合 @ResponseBody
@RequestMapping("/index")
public class IndexController {
    @RequestMapping("")
    public String index(Model model){
        model.addAttribute("pageTitle", "表单提交系统");
        return "adminlist";
    }

}

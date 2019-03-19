package com.my.email.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.common.CommonOperation;
import com.my.common.exception.JsonException;
import com.my.common.result.ErrorCodes;
import com.my.email.model.Email;
import com.my.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Value("${list.pagesize}")
    private Integer pageSize;
    @Value("${file.email-image-path}")
    private String imageSavePath;

    @RequestMapping("/list")
    public String emailList(@RequestParam(value = "title", required = false, defaultValue = "")String title,
                            @RequestParam(value = "page", required = false)Integer page,
                            HttpServletRequest request,
                            ModelMap model){
        try {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("order", "id desc");
            String currentUrl = request.getRequestURI();
            if(title!=null && !title.isEmpty()){
                filter.put("title", title);
                currentUrl += "?title="+title;
            }
            if(page == null || page<1){
                page = 1;
            }
            int totalCount = emailService.getCount(filter);
            int pageCount = (int)Math.ceil(totalCount/pageSize);
            if(pageCount <1){
                pageCount = 1;
            }

            filter.put("page", (page-1)*pageSize);
            filter.put("pagesize", pageSize);

            List<Email> list = emailService.getList(filter);

            model.addAttribute("list", list);

            model.addAttribute("currentPage", page);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("currentUrl", currentUrl);

            model.addAttribute("pageTitle","邮件列表 - 邮件管理平台 - 后台管理系统");
            model.addAttribute("TopMenuFlag", "email");
            return "email/email_list";
        }catch (JsonException e){
            return "error/500";
        }
    }

    @RequestMapping("/send")
    public String sendEmail(ModelMap model){
        model.addAttribute("pageTitle","发送邮件 - 邮件管理平台 - 后台管理系统");

        model.addAttribute("TopMenuFlag", "email");
        return "email/email_send";
    }

    @ResponseBody
    @RequestMapping("/send/submit")
    public JSONObject sendEmail(Email email){
        JSONObject result = new JSONObject();
        try {
            int rs = emailService.add(email);
            result.put("code", 1);
            result.put("msg", "发送成功（"+rs+"）");
        }catch (JsonException e){
            result = e.toJson();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/upload")
    public JSONObject uploadIamge(@RequestParam(value = "fileupload")MultipartFile file){
        JSONObject result = new JSONObject();
        try {
            result = CommonOperation.uploadFile(file, imageSavePath);
            result.put("path", "/emailread/getimg?filename="+result.get("realname"));
        }catch (JsonException e){
            result = e.toJson();
        }
        return  result;
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JSONObject emailDelete(@RequestParam(value = "id", required = true)Integer id){
        JSONObject result = new JSONObject();
        try {
            emailService.remove(id);
            result.put("code", 1);
            result.put("msg", "删除成功！");
        }catch (JsonException e){
            result = e.toJson();
        }
        return result;
    }
}

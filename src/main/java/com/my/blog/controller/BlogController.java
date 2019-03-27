package com.my.blog.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.my.blog.model.Blog;
import com.my.blog.service.BlogService;
import com.my.common.CommonOperation;
import com.my.common.aop.Permission;
import com.my.common.exception.JsonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Permission("1007")
@Controller
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Value("${blog.weburl}")
    private String blogWebUrl;

    @Value("${file.blog-image-path}")
    private String imageSavePath;

    @Value("${admin.account}")
    private String adminAccount;

    @Value("${list.pagesize}")
    private Integer pageSize;
    @RequestMapping(value = "/admin/list", method = RequestMethod.GET)
    public String blogList(@RequestParam(value = "title", required = false, defaultValue = "")String title,
                           @RequestParam(value = "author", required = false, defaultValue = "")String author,
                           @RequestParam(value = "page", required = false, defaultValue = "1")Integer page,
                            HttpServletRequest request,
                            ModelMap model){
        try{
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("order", "addtime desc");
            String currentUrl = request.getRequestURI();
            if(title!=null && !title.isEmpty()){
                filter.put("title", title);
                currentUrl += "?title="+title;
            }
            if(author!=null && !author.isEmpty()){
                filter.put("author", author);
                currentUrl += (currentUrl.contains("?")?"&":"?")+"author"+author;
            }
            if(page == null || page<1){
                page = 1;
            }
            int totalCount = blogService.getCount(filter);
            int pageCount = (int)Math.ceil(totalCount/pageSize);
            if(pageCount <1){
                pageCount = 1;
            }

            filter.put("page", (page-1)*pageSize);
            filter.put("pagesize", pageSize);

            List<Blog> taskList = blogService.getList(filter);

            model.addAttribute("list", taskList);
            model.addAttribute("title",title);
            model.addAttribute("author", author);
            model.addAttribute("BLOG_WEB", blogWebUrl);

            model.addAttribute("currentPage", page);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("currentUrl", currentUrl);
            model.addAttribute("pageTitle","贴子列表 - 论坛 - 后台管理系统");

            model.addAttribute("TopMenuFlag", "blog");
            return "blog/blog_list";
        }catch (JSONException e){
            model.addAttribute("error", e);
            return "error/500";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JSONObject blogRemove(@RequestParam(value = "id", required = true)Integer id){
        JSONObject result = new JSONObject();
        try {
            blogService.remove(id);
            result.put("code", 1);
            result.put("msg", "删除成功！");
        }catch (JsonException e){
            result = e.toJson();
        }
        return result;
    }

    @RequestMapping("/admin/add")
    public String add(ModelMap model){
        model.addAttribute("pageTitle","创建贴子 - 论坛 - 后台管理系统");
        model.addAttribute("TopMenuFlag", "blog");
        return "blog/blog_add";
    }

    @ResponseBody
    @RequestMapping("/upload")
    public JSONObject uploadIamge(@RequestParam(value = "fileupload")MultipartFile file){
        JSONObject result = new JSONObject();
        try {
            result = CommonOperation.uploadFile(file, imageSavePath);
            result.put("path", "/blog/getimg?filename="+result.get("realname"));
        }catch (JsonException e){
            result = e.toJson();
        }
        return  result;
    }

    @ResponseBody
    @RequestMapping("/admin/add/submit")
    public JSONObject add(Blog blog, HttpSession session){
        JSONObject rs = new JSONObject();
        try {
            blog.setAuthor(session.getAttribute(adminAccount).toString());
            String content = blog.getContent();
            content = content.replaceAll("<[.[^>]]*>","");
            content = content.replaceAll(" ", "");
            blog.setIntro(content.substring(0,500));
            blogService.add(blog);
            rs.put("code", 1);
            rs.put("msg", "添加成功");
        }catch (JsonException e){
            rs = e.toJson();
        }
        return rs;
    }

    @RequestMapping("/admin/edit/{id}")
    public String edit(@PathVariable("id")Integer id, ModelMap model){
        try {
            Blog blog = blogService.get(id);
            model.addAttribute("blog", blog);
            model.addAttribute("pageTitle","创建贴子 - 论坛 - 后台管理系统");
            model.addAttribute("TopMenuFlag", "blog");
        }catch (JsonException e){
            return "error/404";
        }
        return "blog/blog_edit";
    }
}

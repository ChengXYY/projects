package com.my.blog.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.my.blog.model.Blog;
import com.my.blog.service.BlogService;
import com.my.common.exception.JsonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Value("${blog.weburl}")
    private String blogWebUrl;

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
}

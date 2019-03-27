package com.my.blog.controller.api;

import com.my.blog.model.Blog;
import com.my.blog.service.BlogService;
import com.my.common.exception.JsonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogApi {
    @Autowired
    private BlogService blogService;

    @Value("${blog.weburl}")
    private String blogWebUrl;

    @Value("${list.pagesize}")
    private Integer pageSize;


    @RequestMapping("/list")
    public List<Blog> blogList(@RequestParam(value = "title", required = false, defaultValue = "")String title,
                               @RequestParam(value = "author", required = false, defaultValue = "")String author,
                               @RequestParam(value = "page", required = false, defaultValue = "1")Integer page){

            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("order", "addtime desc");
            if (title != null && !title.isEmpty()) {
                filter.put("title", title);
            }
            if (author != null && !author.isEmpty()) {
                filter.put("author", author);
            }
            if (page == null || page < 1) {
                page = 1;
            }
            int totalCount = blogService.getCount(filter);
            int pageCount = (int) Math.ceil(totalCount / pageSize);
            if (pageCount < 1) {
                pageCount = 1;
            }

            filter.put("page", (page - 1) * pageSize);
            filter.put("pagesize", pageSize);

            return blogService.getList(filter);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Blog blogDetail(@RequestParam(value = "id", required = true)Integer id){
        try {
            return blogService.get(id);
        }catch (JsonException e){
            return null;
        }
    }

}

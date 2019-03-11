package com.my.blog.service;

import com.my.blog.model.Blog;

import java.util.List;
import java.util.Map;

public interface BlogService {
    //add
    int add(Blog blog);
    //edit
    int edit(Map<String,Object>blog);
    //remove
    int remove(Integer id);
    //get one
    Blog get(Integer id);
    //get list
    List<Blog>getList(Map<String,Object>filter);
}

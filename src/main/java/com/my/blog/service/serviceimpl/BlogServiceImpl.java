package com.my.blog.service.serviceimpl;

import com.my.blog.model.Blog;
import com.my.blog.service.BlogService;
import com.my.common.exception.JsonException;
import com.my.common.result.ErrorCodes;

import java.util.List;
import java.util.Map;

public class BlogServiceImpl implements BlogService {
    @Override
    public int add(Blog blog) {
        if(blog.getTitle().isEmpty())throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        return 0;
    }

    @Override
    public int edit(Map<String, Object> blog) {
        return 0;
    }

    @Override
    public int remove(Integer id) {
        return 0;
    }

    @Override
    public Blog get(Integer id) {
        return null;
    }

    @Override
    public List<Blog> getList(Map<String, Object> filter) {
        return null;
    }
}

package com.my.blog.service.serviceimpl;

import com.my.blog.mapper.BlogMapper;
import com.my.blog.model.Blog;
import com.my.blog.service.BlogService;
import com.my.common.CommonOperation;
import com.my.common.exception.JsonException;
import com.my.common.result.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public int add(Blog blog) {
        if(blog.getTitle().isEmpty())throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        return blogMapper.insertSelective(blog);
    }

    @Override
    public int edit(Map<String, Object> blog) {
        if(blog.get("id")==null || CommonOperation.checkId(Integer.parseInt(blog.get("id").toString())))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        return blogMapper.updateByPrimaryKeySelective(blog);
    }

    @Override
    public int remove(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        int rs = blogMapper.deleteByPrimaryKey(id);
        if(rs == 1)
            return 1;
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public Blog get(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Blog blog = blogMapper.selectByPrimaryKey(id);
        if(blog == null){
            throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        }
        return blog;
    }

    @Override
    public List<Blog> getList(Map<String, Object> filter) {
        return blogMapper.selectByFilter(filter);
    }

    @Override
    public int getCount(Map<String, Object> filter) {
        return blogMapper.countByFilter(filter);
    }

}

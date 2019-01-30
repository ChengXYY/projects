package com.my.formtool.service.serviceimpl;

import com.my.formtool.mapper.AdminMapper;
import com.my.formtool.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public int add(Map<String, Object> admin) {
        return adminMapper.insertSelective(admin);
    }

    @Override
    public int edit(Map<String, Object> admin) {
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public int remove(Integer id) {
        return adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Map<String, Object> get(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Map<String, Object>> getList(Map<String, Object> filter) {
        return adminMapper.selectByFilter(filter);
    }
}

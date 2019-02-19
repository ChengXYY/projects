package com.my.unitadmin.service.serviceimpl;

import com.my.common.exception.JsonException;
import com.my.unitadmin.mapper.AdminlogMapper;
import com.my.unitadmin.model.Adminlog;
import com.my.common.result.ErrorCodes;
import com.my.unitadmin.service.AdminlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminlogServiceImpl implements AdminlogService {

    @Autowired
    private AdminlogMapper adminlogMapper;

    @Override
    public int add(String admin, String content) {
        if(admin.isEmpty() || content.isEmpty())throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        Adminlog log = new Adminlog();
        log.setContent(content);
        log.setAdmin(admin);
        return adminlogMapper.insertSelective(log);
    }

    @Override
    public List<Adminlog> getList(Map<String, Object> filter) {
        return adminlogMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        return adminlogMapper.countByFilter(filter);
    }
}

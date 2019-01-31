package com.my.formtool.service.serviceimpl;

import com.my.formtool.common.CommonOperation;
import com.my.formtool.exception.ErrorCodes;
import com.my.formtool.exception.JsonException;
import com.my.formtool.mapper.AdmingroupMapper;
import com.my.formtool.model.Admingroup;
import com.my.formtool.service.AdmingroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmingroupServiceImpl implements AdmingroupService {

    @Autowired
    private AdmingroupMapper admingroupMapper;

    @Override
    public List<Admingroup> getListAll() {
        return admingroupMapper.selectAll();
    }

    @Override
    public int add(Admingroup admingroup) {
        if(admingroup.getName().isEmpty() || !CommonOperation.checkId(admingroup.getSort())) throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        int rs = admingroupMapper.insertSelective(admingroup);
        if(rs>0)
            return rs;
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public int edit(Admingroup admingroup) {
        return 0;
    }

    @Override
    public int remove(Integer id) {
        return 0;
    }

    @Override
    public Admingroup get(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Admingroup admingroup = admingroupMapper.selectByPrimaryKey(id);
        if(admingroup == null)throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return admingroup;
    }
}

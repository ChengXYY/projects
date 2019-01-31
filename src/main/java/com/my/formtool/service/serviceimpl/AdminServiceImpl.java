package com.my.formtool.service.serviceimpl;

import com.my.formtool.common.CommonOperation;
import com.my.formtool.exception.ErrorCodes;
import com.my.formtool.exception.JsonException;
import com.my.formtool.mapper.AdminMapper;
import com.my.formtool.model.Admin;
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
    public int add(Admin admin){
        if(admin.getAccount().isEmpty() ||
                admin.getGroupid().toString().equals("0") ||
                admin.getPassword().isEmpty()) throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        String pwd = admin.getPassword();
        Map<String,Object>pwdArr = CommonOperation.encodeStr(pwd);
        admin.setSalt(pwdArr.get("salt").toString());
        admin.setPassword(pwdArr.get("newstr").toString());
        int rs = adminMapper.insertSelective(admin);
        if(rs>0)
            return rs;
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public int edit(Admin admin) {
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public int remove(Integer id) {
        return adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Admin get(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Admin> getList(Map<String, Object> filter) {
        return adminMapper.selectByFilter(filter);
    }

    @Override
    public int resetPassword(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Admin admin = get(id);
        if(admin == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        Map<String,Object>pwdArr = CommonOperation.encodeStr("123456");
        admin.setSalt(pwdArr.get("salt").toString());
        admin.setPassword(pwdArr.get("newstr").toString());
        int rs = edit(admin);
        if(rs>=0)
            return 1;
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }
}

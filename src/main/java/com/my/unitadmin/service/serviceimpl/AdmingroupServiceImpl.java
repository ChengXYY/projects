package com.my.unitadmin.service.serviceimpl;

import com.my.common.CommonOperation;
import com.my.common.result.ErrorCodes;
import com.my.common.exception.JsonException;
import com.my.unitadmin.mapper.AdmingroupMapper;
import com.my.unitadmin.model.Admingroup;
import com.my.unitadmin.service.AdmingroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdmingroupServiceImpl implements AdmingroupService {

    @Autowired
    private AdmingroupMapper admingroupMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Admingroup> getListAll(Integer parentid) {
        return admingroupMapper.selectAll(parentid);
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
    public int edit(Map<String, Object> admingroup) {
        if(admingroup.get("id")==null || !CommonOperation.checkId(Integer.parseInt(admingroup.get("id").toString())))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        return admingroupMapper.updateByPrimaryKeySelective(admingroup);
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

    @Override
    public void changeAuth(Integer id, String[] auths) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        if(auths.length>0){
            String authStr = "";
            for (String code : auths){
                authStr += code+"|";
            }
            authStr.substring(0,authStr.length()-1);
            Map<String, Object> group = new HashMap<>();
            group.put("id", id);
            group.put("auth", authStr);
            admingroupMapper.updateByPrimaryKeySelective(group);
        }
    }


}

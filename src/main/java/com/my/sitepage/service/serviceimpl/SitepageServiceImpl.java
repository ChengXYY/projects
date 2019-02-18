package com.my.sitepage.service.serviceimpl;

import com.my.common.CommonOperation;
import com.my.common.exception.JsonException;
import com.my.common.result.ErrorCodes;
import com.my.sitepage.mapper.SitepageMapper;
import com.my.sitepage.model.Sitepage;
import com.my.sitepage.service.SitepageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SitepageServiceImpl implements SitepageService {

    @Autowired
    private SitepageMapper sitepageMapper;

    @Override
    public int add(Sitepage page) {
        if(page.getCode().isEmpty())throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        int rs = sitepageMapper.insertSelective(page);
        if(rs>0){
            return rs;
        }else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public int edit(Map<String, Object> page) {
        if(page.get("id")== null || !CommonOperation.checkId(Integer.parseInt(page.get("id").toString())))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Sitepage site = sitepageMapper.selectByPrimaryKey(Integer.parseInt(page.get("id").toString()));
        if(site == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return sitepageMapper.updateByPrimaryKeySelective(page);
    }

    @Override
    public int remove(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        return sitepageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Sitepage get(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Sitepage sitepage = sitepageMapper.selectByPrimaryKey(id);
        if(sitepage == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return sitepage;
    }

    @Override
    public List<Sitepage> getList(Map<String, Object> filter) {
        return sitepageMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        return sitepageMapper.countByFilter(filter);
    }
}

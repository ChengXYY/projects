package com.my.filemanager.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.my.common.CommonOperation;
import com.my.common.exception.JsonException;
import com.my.common.result.ErrorCodes;
import com.my.filemanager.mapper.FilemanagerMapper;
import com.my.filemanager.model.Filemanager;
import com.my.filemanager.service.FilemanagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class FilemanagerServiceImpl implements FilemanagerService {
    @Autowired
    private FilemanagerMapper filemanagerMapper;

    @Override
    public int add(Filemanager file) {
        if(file.getName().isEmpty() || file.getPath().isEmpty())throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        int rs = filemanagerMapper.insertSelective(file);
        if(rs>0){
            return rs;
        }else throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);

    }

    @Override
    public int edit(Map<String, Object> file) {
        if(file.get("id")== null || !CommonOperation.checkId(Integer.parseInt(file.get("id").toString())))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Filemanager fileObj = get(Integer.parseInt(file.get("id").toString()));
        if(fileObj == null)throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);

        return filemanagerMapper.updateByPrimaryKeySelective(file);
    }

    @Override
    public int remove(Integer id) {
        Filemanager filemanager = get(id);
        File file = new File(filemanager.getPath());
        if(file.exists()){
            file.delete();
        }
        return filemanagerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Filemanager get(Integer id) {
        if(id == null || !CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Filemanager file = filemanagerMapper.selectByPrimaryKey(id);
        if(file == null)throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return file;
    }

    @Override
    public List<Filemanager> getList(Map<String, Object> filter) {
        return filemanagerMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        return filemanagerMapper.countByFilter(filter);
    }

    @Override
    public Filemanager upload(MultipartFile file, String savePath) {

        Filemanager filemanager = new Filemanager();
        JSONObject rs = new JSONObject();

        rs = CommonOperation.uploadFile(file, savePath);
        filemanager.setName(rs.getString("filename"));
        filemanager.setPath(savePath + "/"+ rs.getString("realname"));
        filemanager.setSize(rs.getInteger("size"));

        return filemanager;
    }
}

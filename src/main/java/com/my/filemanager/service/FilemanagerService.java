package com.my.filemanager.service;

import com.my.filemanager.model.Filemanager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FilemanagerService {

    // 数据库操作相关
    //add
    int add(Filemanager file);
    //edit
    int edit(Map<String, Object>file);
    //remove
    int remove(Integer id);
    //get one
    Filemanager get(Integer id);
    //get list
    List<Filemanager>getList(Map<String, Object>filter);
    //get count
    Integer getCount(Map<String, Object>filter);

    //非数据库操作
    //upload
    void upload(MultipartFile file, String savePath);
}

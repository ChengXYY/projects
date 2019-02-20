package com.my.restapi;

import com.my.filemanager.model.Filemanager;
import com.my.filemanager.service.FilemanagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/filemanager")
public class FilemanagerApi {
    @Autowired
    private FilemanagerService filemanagerService;

    @Value("${list.pagesize}")
    private Integer pageSize;

    @RequestMapping("/list")
    public List<Filemanager> getList(@RequestParam(value = "name",required = false)String name,
                                     @RequestParam(value = "page", required = false)Integer page){

        Map<String, Object> filter = new HashMap<>();
        filter.put("order", "id desc");
        if(name!=null && !name.isEmpty()){
            filter.put("name",name);
        }else{
            name = "";
        }
        if(page == null || page<1){
            page = 1;
        }
        filter.put("page", (page-1)*pageSize);
        filter.put("pagesize", pageSize);
        List<Filemanager> list = filemanagerService.getList(filter);
        return list;
    }
}

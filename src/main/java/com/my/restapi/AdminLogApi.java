package com.my.restapi;

import com.my.unitadmin.model.Adminlog;
import com.my.unitadmin.service.AdminlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adminlog")
public class AdminLogApi {

    @Autowired
    private AdminlogService adminlogService;

    @Value("${list.pagesize}")
    private Integer pageSize;

    //list
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Adminlog> getList(@RequestParam(value = "content",required = false)String content,
                        @RequestParam(value = "page", required = false)Integer page){

        Map<String, Object> filter = new HashMap<>();
        filter.put("order", "id desc");
        if(content!=null && !content.isEmpty()){
            filter.put("content",content);
        }else{
            content = "";
        }
        if(page == null || page<1){
            page = 1;
        }
        filter.put("page", (page-1)*pageSize);
        filter.put("pagesize", pageSize);
        List<Adminlog> list = adminlogService.getList(filter);
        return list;
    }
}

package com.my.filemanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.common.aop.Permission;
import com.my.common.exception.JsonException;
import com.my.filemanager.model.Filemanager;
import com.my.filemanager.service.FilemanagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file")
@Permission("1005")
public class FilemanagerController {
    @Autowired
    private FilemanagerService filemanagerService;

    @Value("${file.save-path}")
    private String savePath;

    @Value("${list.pagesize}")
    private Integer pageSize;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String fileList(@RequestParam(value = "name", required = false)String name,
                            @RequestParam(value = "page", required = false)Integer page,
                            HttpServletRequest request,
                            ModelMap model){
        try {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("order", "id desc");
            String currentUrl = request.getRequestURI();
            if(name!=null && !name.isEmpty()){
                filter.put("name", name);
                currentUrl += "?name="+name;
            }
            if(page == null || page<1){
                page = 1;
            }
            int totalCount = filemanagerService.getCount(filter);
            int pageCount = (int)Math.ceil(totalCount/pageSize);
            if(pageCount <1){
                pageCount = 1;
            }

            filter.put("page", (page-1)*pageSize);
            filter.put("pagesize", pageSize);

            List<Filemanager> list = filemanagerService.getList(filter);

            model.addAttribute("list", list);

            model.addAttribute("currentPage", page);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("currentUrl", currentUrl);

            model.addAttribute("pageTitle","文件列表 - 文件管理平台 - 后台管理系统");
            model.addAttribute("TopMenuFlag", "filemanager");
            return "/filemanager/file_list";
        }catch (JsonException e){
            return "/error/500";
        }
    }

    @ResponseBody
    @RequestMapping("/upload")
    public JSONObject fileUpload(@RequestParam(value = "fileupload")MultipartFile file){
        JSONObject result = new JSONObject();
        try {
            Filemanager filemanager = filemanagerService.upload(file, savePath);
            filemanagerService.add(filemanager);
            result.put("code", 1);
            result.put("msg", "上传成功");
            return result;
        }catch (JsonException e){
            //result.put("code", e.getCode());
            //result.put("msg", e.getMsg());
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JSONObject fileDelete(@RequestParam(value = "id", required = true)Integer id){
        JSONObject result = new JSONObject();
        try {
            filemanagerService.remove(id);
            result.put("code", 1);
            result.put("msg", "删除成功");
            return result;
        }catch (JsonException e){
            //result.put("code", e.getCode());
            //result.put("msg", e.getMsg());
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping("/download/{id}")
    public void fileDownload(@PathVariable(value = "id", required = true)Integer id, HttpServletResponse response){

        try {
            Filemanager file = filemanagerService.get(id);
            // 读到流中
            InputStream inStream = new FileInputStream(file.getPath());// 文件的存放路径
            // 设置输出的格式
            response.reset();
            response.setContentType("bin");
            response.addHeader("Content-Disposition",
                            "attachment; filename=" + file.getName() + ";filename*=utf-8''"+URLEncoder.encode(file.getName(),"UTF-8"));
            // 循环取出流中的数据
            byte[] b = new byte[100];
            int len;
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JsonException e){
            System.out.println(e.getMsg());
        }

    }

}

package com.my.formtool.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.my.common.aop.Permission;
import com.my.common.exception.JsonException;
import com.my.formtool.model.Form;
import com.my.formtool.model.Task;
import com.my.formtool.service.FormService;
import com.my.formtool.service.TaskService;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Permission("1003")
@Controller
@RequestMapping("/form")
public class FormController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Value("${list.pagesize}")
    private Integer pageSize;

    @RequestMapping("/list/{id}")
    public String list(@PathVariable(value = "id", required = true)Integer id,
                       @RequestParam(value = "page", required = false)Integer page,
                       HttpServletRequest request,
                       ModelMap model){
        try {
            String currentUrl = request.getRequestURI();

            Task task = taskService.get(id);
            Map<String, Object>filter = new HashMap<>();
            filter.put("taskid", id);
            int totalCount = formService.getCount(filter);
            if(page == null || page<1){
                page = 1;
            }
            int pageCount = (int)Math.ceil(totalCount/pageSize);
            if(pageCount <1) {
                pageCount = 1;
            }

            filter.put("page", (page-1)*pageSize);
            filter.put("pagesize", pageSize);

            List<Form> formList = formService.getList(filter);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("currentUrl", currentUrl);

            model.addAttribute("task", task);
            model.addAttribute("list", formList);

            model.addAttribute("pageTitle","表单列表 - 表单提交平台 - 后台管理系统");
            model.addAttribute("TopMenuFlag", "formtool");

            return "formtool/form_list";
        }catch (JsonException e){
            String returnUrl = "error/";
            switch (e.getCode()){
                case ID_NOT_ILLEGAL:
                    returnUrl += "404";
                    break;
                default:
                    returnUrl += "500";
                    break;
            }
            model.addAttribute("error", e);
            return returnUrl;
        }
    }

    //图表
    @RequestMapping("/chart/{id}")
    public String charts(@PathVariable(value = "id")Integer id, ModelMap model){
        try {
            Task task = taskService.get(id);
            Integer chartCount = formService.getChartCount(id);
            model.addAttribute("task", task);
            model.addAttribute("chartCount", chartCount);
            model.addAttribute("pageTitle","表单数据分析 - 表单提交平台 - 后台管理系统");
            model.addAttribute("TopMenuFlag", "formtool");
            return "formtool/form_chart";
        }catch (JsonException e){
            return "error/404";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/chart/data", method = RequestMethod.POST)
    public JSONObject getData(@RequestParam(value = "id", required = true)Integer taskid){
        try{
            return formService.chartData(taskid);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping(value = "/userform/{id}", method = RequestMethod.GET)
    public String getUserForm(@PathVariable(value = "id")Integer id,
                                ModelMap model){
        try {
            Form form = formService.get(id);
            Map<String, Object> userForm = JSONObject.parseObject(form.getForm(), Map.class);
            Task task = taskService.get(form.getTaskid());
            List<Map> taskField = JSONArray.parseArray(task.getFields(), Map.class);
            model.addAttribute("userForm", userForm);
            model.addAttribute("taskField", taskField);
            return "formtool/form_userform";
        }catch (JsonException e){
            return "error/404";
        }
    }

    //导出Excel
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void excelDownload(@RequestParam(value = "id", required = true)Integer taskid,
                              HttpServletResponse response){
        //---------------获取数据
        //获取问题列表（表头数据）
        Task task = taskService.get(taskid);
        List<Map> fieldList = task.getFieldList().toJavaList(Map.class);
        //获取表单数据
        Map<String, Object>filter = new HashMap<>();
        filter.put("taskid", taskid);
        List<Form> formList = formService.getList(filter);

        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //生成一个表格，设置表格名称为"学生表"
        HSSFSheet sheet = workbook.createSheet("用户表单");

        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);

        //创建第一行表头
        HSSFRow headrow = sheet.createRow(0);

        //添加表头
        HSSFCell cell0 = headrow.createCell(0);
        HSSFRichTextString text0 = new HSSFRichTextString("提交账号");
        cell0.setCellValue(text0);
        HSSFCell cell1 = headrow.createCell(1);
        HSSFRichTextString text1 = new HSSFRichTextString("提交时间");
        cell1.setCellValue(text1);
        for (int i = 0; i < fieldList.size(); i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i+2);
            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(fieldList.get(i).get("name").toString());
            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
        }

        //把内容加入表格
        for (int i=0; i<formList.size(); i++){
            HSSFRow row = sheet.createRow(i+1); //从第2行开始
            //第一、二列添加
            HSSFCell cell00 = row.createCell(0);
            HSSFRichTextString text00 = new HSSFRichTextString(formList.get(i).getUser());
            cell00.setCellValue(text00);
            HSSFCell cell01 = row.createCell(1);
            HSSFRichTextString text01 = new HSSFRichTextString(formList.get(i).getAddtime().toLocalDateTime().toString());
            cell01.setCellValue(text01);

            for(int j=0; j<fieldList.size(); j++){ //按列添加数据
                HSSFCell cell = row.createCell(j+2);
                String id = fieldList.get(j).get("id").toString();
                JSONObject userForm = JSONObject.parseObject(formList.get(i).getForm());
                cell.setCellValue(userForm.get(id).toString());
            }
        }
        try {
            //准备将Excel的输出流通过response输出到页面下载
            //八进制输出流
            response.setContentType("application/octet-stream");

            //这后面可以设置导出Excel的名称，此例中名为student.xls
            response.setHeader("Content-disposition", "attachment;filename=student.xls");

            //刷新缓冲
            response.flushBuffer();

            //workbook将Excel写入到response的输出流中，供页面下载
            workbook.write(response.getOutputStream());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}

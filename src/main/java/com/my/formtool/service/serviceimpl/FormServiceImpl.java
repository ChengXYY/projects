package com.my.formtool.service.serviceimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.my.common.CommonOperation;
import com.my.common.exception.JsonException;
import com.my.formtool.mapper.FormMapper;
import com.my.formtool.model.ChartData;
import com.my.formtool.model.Form;
import com.my.formtool.model.Task;
import com.my.common.result.ErrorCodes;
import com.my.formtool.service.FormService;
import com.my.formtool.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class FormServiceImpl implements FormService {
    @Autowired
    private FormMapper formMapper;

    @Autowired
    private TaskService taskService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int submit(Map<String, Object> data) {logger.info(data.toString());
        if(data.get("taskid") == null
                || data.get("taskid").toString().isEmpty()
                || !CommonOperation.checkId(Integer.parseInt(data.get("taskid").toString())))
            throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);

        Task task = taskService.get(Integer.parseInt(data.get("taskid").toString()));
        if(task == null)throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);

        List<Map>fieldList = task.getFieldList().toJavaList(Map.class);
        JSONObject formArr = new JSONObject();
        for (int i=0; i<fieldList.size(); i++){
            if(fieldList.get(i).get("isnessary").toString().equals("1")){
                if(data.get(fieldList.get(i).get("id")) == null)throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
            }
            formArr.put(fieldList.get(i).get("id").toString(), data.get(fieldList.get(i).get("id")));
        }
        Form form = new Form();
        form.setTaskid(task.getId());
        form.setForm(formArr.toString());
        form.setStatus(2);
        return formMapper.insertSelective(form);
    }

    @Override
    public List<Form> getList(Map<String, Object> filter) {
        if(filter.get("taskid")==null || !CommonOperation.checkId(Integer.parseInt(filter.get("taskid").toString()))) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        return formMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        if(filter.get("taskid")==null || !CommonOperation.checkId(Integer.parseInt(filter.get("taskid").toString()))) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        return formMapper.countByFilter(filter);
    }

    //生成图表数据
    @Override
    public JSONObject chartData(Integer taskid) {
        //获取问题
        Task task = taskService.get(taskid);
        List<Map>fieldList = task.getFieldList().toJavaList(Map.class); //问题列表

        //获取表单
       List<String> formItemList = formMapper.getForms(taskid);
        try {
            JSONArray dataList = new JSONArray();
            //处理字段数据
            for (int i=0; i<fieldList.size(); i++){
                //只做单选和复选的统计图
                if(fieldList.get(i).get("type").equals("text")){
                    continue;
                }
                JSONObject data = new JSONObject();
                data.put("name", fieldList.get(i).get("name"));
                String id =  fieldList.get(i).get("id").toString();
                //选项处理
                List<String> itemList = JSONArray.parseArray(fieldList.get(i).get("item").toString(), String.class);
                JSONArray itemListNew = new JSONArray();
                for (int j=0; j<itemList.size(); j++){
                    ChartData chartData = new ChartData();
                    chartData.setName(itemList.get(j));
                    //在所有表单中计数
                    for(int k=0; k<formItemList.size(); k++){
                        JSONObject formItem = JSONObject.parseObject(formItemList.get(k));
                        if(formItem.get(id).toString().equals(itemList.get(j))){
                            chartData.addOne();
                        }
                    }
                    itemListNew.add(chartData);
                }System.out.println(i);
                data.put("itemlist", itemListNew);
                dataList.add(data);
            }
            JSONObject rs = new JSONObject();
            rs.put("data", dataList);
            rs.put("code", 1);
            rs.put("msg", "成功取得数据");
            return rs;
        }catch (NullPointerException e){
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    public Integer getChartCount(Integer taskid) {
        //获取问题
        Task task = taskService.get(taskid);
        List<Map>fieldList = task.getFieldList().toJavaList(Map.class);
        int count = 0;
        for (int i=0; i<fieldList.size(); i++){
            if(!fieldList.get(i).get("type").equals("text")){
                count++;
            }
        }
        return count;
    }


}

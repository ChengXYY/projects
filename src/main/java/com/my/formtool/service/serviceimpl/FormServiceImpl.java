package com.my.formtool.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.my.common.CommonOperation;
import com.my.common.exception.JsonException;
import com.my.formtool.mapper.FormMapper;
import com.my.formtool.model.Form;
import com.my.formtool.model.Task;
import com.my.common.result.ErrorCodes;
import com.my.formtool.service.FormService;
import com.my.formtool.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

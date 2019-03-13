package com.my.common;

import com.alibaba.fastjson.JSONObject;
import com.my.common.exception.JsonException;
import com.my.common.result.ErrorCodes;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonOperation {
    public static Map<String, Object> encodeStr(String str){
        Map<String, Object> rs = new HashMap<String, Object>();
        //get salt
        String salt = UUID.randomUUID().toString();
        rs.put("salt", salt);
        //md5
        String str1 = DigestUtils.md5DigestAsHex((str+salt).getBytes());
        rs.put("newstr",str1);
        return rs;
    }

    public static String encodeStr(String str, String salt){
        String str1 = DigestUtils.md5DigestAsHex((str+salt).getBytes());
        return str1;
    }

    public static boolean checkId(Integer id){
        if(id.toString().isEmpty()) return false;
        if(id.intValue() < 1)return false;
        return true;
    }

    public static boolean checkEmail(String email){
        if(email == null || email.isEmpty())return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(email);
        if (m.matches())
            return true;
        else
            return false;
    }

    //返回值
    public static JSONObject uploadFile(MultipartFile file, String savePath){
        JSONObject rs = new JSONObject();
        if(file == null)throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        String fileName = file.getOriginalFilename();
        String newFileName = System.currentTimeMillis() + "_" +fileName;
        int size = (int)file.getSize();
        size = (int)Math.ceil(size/1024);
        if(size <= 0)throw JsonException.newInstance(ErrorCodes.FILE_UPLOAD_ERROR);
        String destDir = savePath + "/" + newFileName;
        File dest  = new File(destDir);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest);
            rs.put("code", 1);
            rs.put("msg", "上传成功");
            rs.put("size", size);
            rs.put("filename", fileName);
            rs.put("realname", newFileName);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
           throw JsonException.newInstance(ErrorCodes.FILE_UPLOAD_ERROR);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw JsonException.newInstance(ErrorCodes.FILE_UPLOAD_ERROR);
        }
        return rs;
    }
}

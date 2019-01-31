package com.my.formtool.common;

import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public static boolean checkId(Integer id){
        if(id.toString().isEmpty()) return false;
        if(id.intValue() < 1)return false;
        return true;
    }
}

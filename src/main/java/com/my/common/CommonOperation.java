package com.my.common;

import org.springframework.util.DigestUtils;

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
}

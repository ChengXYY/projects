package com.my.formtool.model.result;

import java.util.*;

public enum AuthCode {

    Menu_System(1001, "菜单查看权限-系统设置"),
    Menu_Index(1002,"菜单查看权限-首页"),
    Menu_Formtool(1003,"菜单查看权限-表单提交平台"),
    Menu_Users(1004,"菜单查看权限-用户管理");

    private Integer code;
    private String intro;

    AuthCode(Integer code, String intro){
        this.code = code;
        this.intro = intro;
    }

    public Integer getCode(){
        return this.code;
    }
    public String getIntro(){
        return this.intro;
    }
    public static AuthCode fromAuthCode(Integer code){
        for (AuthCode codes : AuthCode.values()) {
            if (Objects.equals(code,codes.getCode())) {
                return codes;
            }
        }
        return null;
    }

    public static List<Map<String, Object>> listAuthCode(){
        List<Map<String, Object>> result = new ArrayList<>();
        for (AuthCode codes : AuthCode.values()) {
            Map<String, Object> item = new HashMap<>();
            item.put("code", codes.getCode());
            item.put("intro", codes.getIntro());
            result.add(item);
        }
        return result;
    }
}

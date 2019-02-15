package com.my.common.result;

import java.util.Objects;

public enum ErrorCodes {
    //3xxxx:通用错误码定义
    //5xxxx:业务相关错误码定义
    //7xxxx:未知错误码
    //8xxxx:Http相关错误码定义
    //9xxxx:统一错误码及第三方服务错误码定义

    PARAM_PARSE_ERROR(30001,"参数解析错误"),
    JSON_PARSE_ERROR(30002,"JSON转换失败"),
    EXISTED_ERROR(50001,"重复添加"),
    USER_NOT_FOUND_ERROR(50002,"用户不存在"),
    PWD_ERROR(50003,"密码错误"),
    GET_CODE_ERROR(50004,"获取短信验证码失败"),
    VERIFY_MOBILE_CODE_ERROR(50005,"短信验证码错误"),
    VERIFY_CODE_ERROR(50006,"验证失败"),
    USER_EXIST_ERROR(50007,"用户名已存在"),
    UN_LOGIN_ERROR(50008,"用户未登录"),
    USER_PWD_ERROR(50009, "用户名或密码错误"),
    PROJECT_CONFIG_ERROR(50010, "项目配置项存在错误"),
    USER_NO_PERMISSION(50011, "无权限操作"),
    AUTHENTICATION_ERROR(50012, "非法令牌或已失效"),
    TOKEN_CREATE_ERROR(50013, "令牌生成失败"),
    ID_NOTEXIST_ERROR(50014, "数据不存在"),
    NAME_EXIST_ERROR(50015, "数据已存在"),
    PARENTS_CHILD_SAME_ERROR(50016, "父概念与子概念相同"),
    USER_STATUS_ERROR(55017,"账号无权限，请与管理员联系！"),
    FILE_UPLOAD_ERROR(50018,"文件上传失败"),
    FILE_DOWNLOAD_ERROR(50020,"文件下载失败"),
    MANAGE_TYPE_ERROR(50019,"管理员类型错误"),
    USER_ADMIN_STATUS_ERROR(55021,"账号无权限"),
    UNKNOWN_ERROR(70001,"未知错误"),
    CONNECT_ERROR(70002,"无法连接"),
    HTTP_ERROR(80001,"HTTP相关错误"),
    SERVICE_ERROR(90000,"服务端内部错误"),
    THIRD_PARTY_ERROR(90001,"远程服务错误:"),
    REMOTE_SERVICE_ERROR(90002,"远程服务错误"),
    REMOTE_PARSE_ERROR(90003,"远程数据解析错误"),
    IS_NOT_EMPTY(10001, "字段不能为空"),
    IS_REPEATED(10002, "字段重复"),
    ID_NOT_ILLEGAL(10003, "ID不合法"),
    ITEM_NOT_EXIST(10004, "记录不存在"),
    PASSWORD_ERROR(10005, "密码错误"),
    VERCODE_ERROR(10006, "验证码错误"),
    PASSWORD_NOT_IDENTICAL(10007, "密码不一直"),
    DATA_OP_FAILED(20001, "数据操作失败");

    private Integer code;
    private String errorInfo;

    ErrorCodes(Integer code, String errorInfo){
        this.code = code ;
        this.errorInfo = errorInfo;
    }

    public int getErrorCode() {
        return code;
    }

    public String getInfo() {
        return toString();
    }

    @Override
    public String toString() {
        return errorInfo;
    }

    public static ErrorCodes fromErrorCode(Integer code){
        for (ErrorCodes error : ErrorCodes.values()) {
            if (Objects.equals(code,error.getErrorCode())) {
                return error;
            }
        }
        return null;
    }

}

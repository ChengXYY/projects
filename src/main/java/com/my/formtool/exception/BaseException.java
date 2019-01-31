package com.my.formtool.exception;

public class BaseException extends RuntimeException {

    private ErrorCodes code;
    private String msg;

    protected BaseException(ErrorCodes code) {
        this(code,code.toString());
    }

    protected BaseException(ErrorCodes code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ErrorCodes getCode() {
        return code;
    }

    public void setCode(ErrorCodes code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}

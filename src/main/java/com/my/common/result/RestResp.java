package com.my.common.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResp<T>
{
    public RestResp() {}

    public static enum ActionStatusMethod
    {
        OK("OK"),  FAIL("FAIL");

        private final String name;

        private ActionStatusMethod(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }
    }

    @JsonProperty("ActionStatus")
    private String ActionStatus = ActionStatusMethod.OK
            .toString();
    @JsonProperty("ErrorCode")
    private Integer ErrorCode = Integer.valueOf(0);
    @JsonProperty("ErrorInfo")
    private String ErrorInfo = "";
    private T data;

    public RestResp(Integer code, String msg)
    {
        this.ActionStatus = ActionStatusMethod.FAIL.toString();
        this.ErrorCode = code;
        this.ErrorInfo = msg;
    }

    public RestResp(T data)
    {
        this.data = data;
    }

    @JsonIgnore
    public String getActionStatus()
    {
        return this.ActionStatus;
    }

    public void setActionStatus(String actionStatus)
    {
        this.ActionStatus = actionStatus;
    }

    @JsonIgnore
    public Integer getErrorCode()
    {
        return this.ErrorCode;
    }

    public void setErrorCode(Integer errorCode)
    {
        this.ErrorCode = errorCode;
    }

    @JsonIgnore
    public String getErrorInfo()
    {
        return this.ErrorInfo;
    }

    public void setErrorInfo(String errorInfo)
    {
        this.ErrorInfo = errorInfo;
    }

    public T getData()
    {
        return (T)this.data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}

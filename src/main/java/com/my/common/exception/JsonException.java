package com.my.common.exception;

import com.alibaba.fastjson.JSONObject;
import com.my.common.result.ErrorCodes;

public class JsonException extends BaseException {

    private JsonException(ErrorCodes code) {
        super(code);
    }

    public static JsonException newInstance(){
        return newInstance(ErrorCodes.JSON_PARSE_ERROR);
    }

    public static JsonException newInstance(ErrorCodes code){
        return new JsonException(code);
    }

    public JSONObject toJson(){
        JSONObject res = new JSONObject();
        res.put("code", this.getCode());
        res.put("msg", this.getMsg());
        return res;
    }

}

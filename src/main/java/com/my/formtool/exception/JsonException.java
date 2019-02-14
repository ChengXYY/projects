package com.my.formtool.exception;

import com.my.formtool.model.result.ErrorCodes;

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

}

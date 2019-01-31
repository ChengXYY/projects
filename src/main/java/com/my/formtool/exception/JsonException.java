package com.my.formtool.exception;

import javax.json.JsonObject;

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

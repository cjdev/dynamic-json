package com.cj.dynamicjson.simplejson;

public class JsonParseException extends RuntimeException {

    public JsonParseException(String string) {
        super(string);
    }

    public JsonParseException(String string, Exception e) {
        super(string, e);
    }
}

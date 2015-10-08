package com.cj.dynamicjson.simplejson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class MarshallerImpl{
    public static final MarshallerImpl instance = new MarshallerImpl();
    private final JSONParser parser;
    
    private MarshallerImpl() {
        parser = new JSONParser();
    }


    public SimpleJsonAST parse(String jsonText) {
        return parse(new ByteArrayInputStream(jsonText.getBytes(StandardCharsets.UTF_8)));
    }

    public SimpleJsonAST parse(InputStream jsonText) {
        try {
            return new SimpleJsonAST((Object)parser.parse(new InputStreamReader(jsonText, "UTF-8")));
        } catch (Exception e) {
            throw new JsonParseException("Error Parsing Json", e);
        }
    }
    
}

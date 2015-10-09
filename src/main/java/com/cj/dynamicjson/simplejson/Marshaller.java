package com.cj.dynamicjson.simplejson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.cj.dynamicjson.jackson.MarshallerImpl;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class Marshaller implements com.cj.dynamicjson.Marshaller {
    public static final Marshaller instance = new Marshaller();

    private Marshaller(){}
    
    public SimpleJsonAST parse(String jsonText) {
        return parse(new ByteArrayInputStream(jsonText.getBytes(StandardCharsets.UTF_8)));
    }

    public SimpleJsonAST parse(InputStream jsonText) {
        try {
            return new SimpleJsonAST((Object)new JSONParser().parse(new InputStreamReader(jsonText, "UTF-8")));
        } catch (Exception e) {
            throw new JsonParseException("Error Parsing Json", e);
        }
    }
    
}

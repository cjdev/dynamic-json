package com.cj.dynamicjson.jackson2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Marshaller implements com.cj.dynamicjson.Marshaller {
	private static final ObjectMapper jackson = new ObjectMapper();
    public static final Marshaller instance = new Marshaller();

    private Marshaller() {
    }

    public JacksonAst parse(String jsonText) {
        return parse(new ByteArrayInputStream(jsonText.getBytes(StandardCharsets.UTF_8)));
    }

    public JacksonAst parse(InputStream jsonText) {
    		
    	
        try {
            return new JacksonAst(jackson.readTree(jsonText));
        } catch(Exception e) {
            throw new JsonParseException("Error Parsing Json", e);
        }
    }
}



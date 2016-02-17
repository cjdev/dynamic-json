package com.cj.dynamicjson.simplejson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.cj.dynamicjson.AbstractSyntaxTree;
import com.cj.dynamicjson.IteratorWrapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import org.json.simple.parser.JSONParser;

public class Marshaller implements com.cj.dynamicjson.Marshaller {
    public static final Marshaller instance = new Marshaller();

    private Marshaller() {
    }

    public SimpleJsonAST parse(String jsonText) {
        return parse(new ByteArrayInputStream(jsonText.getBytes(StandardCharsets.UTF_8)));
    }

    public SimpleJsonAST parse(InputStream jsonText) {
        try {
            return new SimpleJsonAST(new JSONParser().parse(new InputStreamReader(jsonText, "UTF-8")));
        } catch(Exception e) {
            throw new JsonParseException("Error Parsing Json", e);
        }
    }
}



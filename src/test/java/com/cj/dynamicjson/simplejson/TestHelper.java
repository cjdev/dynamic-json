package com.cj.dynamicjson.simplejson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;


public class TestHelper {
    public static String loadResourceAsString(String resourceName) {
        InputStream inputStream = TestHelper.class.getClassLoader().getResourceAsStream(resourceName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        int currentChar = read(inputStreamReader);
        while (currentChar != -1) {
            stringBuilder.append((char) currentChar);
            currentChar = read(inputStreamReader);
        }
        close(inputStreamReader);
        return stringBuilder.toString();
    }

//    public static String normalize(String json) {
//        Object parsed = JSONValue.parse(json);
//        String normalized = JSONValue.toJSONString(parsed);
//        return normalized;
//    }

    private static int read(Reader reader) {
        try {
            return reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void close(Reader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

package com.cj.jacksonmapper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TestHelper {
    public static String loadResourceAsString(String resourceName) {
        InputStream inputStream = TestHelper.class.getClassLoader().getResourceAsStream(resourceName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        ReaderWithoutCheckedExceptions reader = new ReaderWithoutCheckedExceptions(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        int currentChar = reader.read();
        while (currentChar != -1) {
            stringBuilder.append((char) currentChar);
            currentChar = reader.read();
        }
        reader.close();
        return stringBuilder.toString();
    }
}

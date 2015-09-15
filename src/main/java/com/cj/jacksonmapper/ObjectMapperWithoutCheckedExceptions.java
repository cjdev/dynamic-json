package com.cj.jacksonmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectMapperWithoutCheckedExceptions {
    private ObjectMapper delegate;

    public ObjectMapperWithoutCheckedExceptions(ObjectMapper delegate) {
        this.delegate = delegate;
    }

    public String writeValueAsString(Object value) {
        try {
            return delegate.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T readValue(String content, Class<T> valueType) {
        try {
            return delegate.readValue(content, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T readValue(String content, JavaType valueType) {
        try {
            return delegate.readValue(content, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

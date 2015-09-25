package com.cj.dynamicjson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.math.BigDecimal;

class UncheckedJsonParser {
    public final JsonParser delegate;

    public UncheckedJsonParser(JsonParser delegate) {
        this.delegate = delegate;
    }

    public JsonToken nextToken() {
        try {
            JsonToken result = delegate.nextToken();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String getText() {
        try {
            return delegate.getText();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public BigDecimal getDecimalValue() {
        try {
            return delegate.getDecimalValue();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String getCurrentName() {
        try {
            return delegate.getCurrentName();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

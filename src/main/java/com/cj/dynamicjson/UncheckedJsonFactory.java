package com.cj.dynamicjson;

import com.fasterxml.jackson.core.JsonFactory;

import java.io.IOException;
import java.io.InputStream;

class UncheckedJsonFactory {
    public final JsonFactory delegate;

    public UncheckedJsonFactory(JsonFactory delegate) {
        this.delegate = delegate;
    }

    public UncheckedJsonParser createParser(String content) {
        try {
            return new UncheckedJsonParser(delegate.createParser(content));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public UncheckedJsonParser createParser(InputStream content) {
        try {
            return new UncheckedJsonParser(delegate.createParser(content));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

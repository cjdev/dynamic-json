package com.cj.jacksonmapper;

import java.io.IOException;
import java.io.Reader;

public class ReaderWithoutCheckedExceptions {
    private Reader delegate;

    public ReaderWithoutCheckedExceptions(Reader delegate) {
        this.delegate = delegate;
    }

    public int read() {
        try {
            return delegate.read();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void close() {
        try {
            delegate.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

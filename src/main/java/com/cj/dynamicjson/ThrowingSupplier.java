package com.cj.dynamicjson;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Exception;
}

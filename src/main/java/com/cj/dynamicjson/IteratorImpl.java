package com.cj.dynamicjson;

import java.util.function.Supplier;

public class IteratorImpl<T> implements java.util.Iterator<T> {

    private final Supplier<T> nextFunction;
    private final Supplier<Boolean> hasNextFunction;

    public IteratorImpl(Supplier<T> next, Supplier<Boolean> hasNext){
        this.nextFunction = next;
        this.hasNextFunction = hasNext;
    }

    @Override
    public boolean hasNext() {
        return hasNextFunction.get();
    }

    @Override
    public T next() {
        return nextFunction.get();
    }
}

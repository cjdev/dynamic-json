package com.cj.dynamicjson;

import java.util.Iterator;
import java.util.function.Supplier;

public class IteratorWrapper<T> implements Iterator<T> {

    private final Supplier<T> nextFunction;
    private final Supplier<Boolean> hasNextFunction;

    public IteratorWrapper(Supplier<T> next, Supplier<Boolean> hasNext){
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

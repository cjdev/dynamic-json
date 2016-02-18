package com.cj.dynamicjson;

public class IteratorImpl<T> implements java.util.Iterator<T> {

    private final ThrowingSupplier<T> nextFunction;
    private final ThrowingSupplier<Boolean> hasNextFunction;

    public IteratorImpl(ThrowingSupplier<T> next, ThrowingSupplier<Boolean> hasNext){
        this.nextFunction = next;
        this.hasNextFunction = hasNext;
    }

    @Override
    public boolean hasNext() {
        return Try.to(hasNextFunction).orElse(false);
    }

    @Override
    public T next() {
        return Try.to(nextFunction).orElseThrow(()->new RuntimeException("Trouble Calling The Next Function"));
    }
}

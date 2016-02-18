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
        try{
            return hasNextFunction.get();
        }catch(Exception e){
            throw new RuntimeException("Trouble Calling The hasNext Function", e);
        }
        
    }

    @Override
    public T next() {
        try{
            return nextFunction.get();
        }catch(Exception e){
            throw new RuntimeException("Trouble Calling The Next Function", e);
        }
    }
}

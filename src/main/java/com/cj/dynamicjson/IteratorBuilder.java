package com.cj.dynamicjson;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IteratorBuilder<T>  {

    Iterator<T> internalIterator = new Iterator<>();

    public IteratorBuilder<T> withNext(ThrowingSupplier<T> supplier){
        internalIterator.nextFunction = supplier;
        return this;
    }
    
    public IteratorBuilder<T> withHasNext(ThrowingSupplier<Boolean> supplier){
        internalIterator.hasNextFunction = supplier;
        return this;
    }
    
    public Iterator<T> iterator(){
        if (internalIterator.hasNextFunction == null || internalIterator.nextFunction == null){
            throw new RuntimeException("You must set both next and hasNext before calling iterator()");
        }
        return internalIterator;
    }
    
    public Stream<T> stream(){
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
    }
    
    @SuppressWarnings("hiding")
    private class Iterator<T> implements java.util.Iterator<T>{
        private ThrowingSupplier<T> nextFunction;
        private ThrowingSupplier<Boolean> hasNextFunction;
    
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
}

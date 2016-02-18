package com.cj.dynamicjson;

public class IteratorBuilder<T>  {
    Iterator internalIterator = new Iterator();
    public IteratorBuilder<T> withNext(ThrowingSupplier<T> supplier){
        internalIterator.nextFunction =  supplier;
        return this;
    }
    
    public IteratorBuilder<T> withHasNext(ThrowingSupplier<Boolean> supplier){
        internalIterator.hasNextFunction=supplier;
        return this;
    }
    
    public Iterator iterator(){
        if (internalIterator.hasNextFunction ==null || internalIterator.nextFunction ==null){
            throw new RuntimeException("You must set both next and hasNext before calling iterator()");
        }
        return internalIterator;
    }
    
    private class Iterator implements java.util.Iterator<T>{
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

package com.cj.dynamicjson;

import java.util.Optional;

public class Try {
    public static <T> Optional<T> to(ThrowingSupplier<T> supplier){
        try{
            return Optional.ofNullable(supplier.get());
        }catch(Exception e){
            return Optional.empty();
        }
    }
}

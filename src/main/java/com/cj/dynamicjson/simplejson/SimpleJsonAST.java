package com.cj.dynamicjson.simplejson;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;

public class SimpleJsonAST implements JsonAst{
 final Object jsonValue;
    
    SimpleJsonAST(Object jsonValue){
        this.jsonValue = jsonValue;
    }
    
    @Override
    public String aString() {
       return tryCatch(()->jsonValue.toString()).orElse("null");
    }

    @Override
    public Optional<String> oString() {
        return Optional.ofNullable(jsonValue).map(Object::toString);
    }

    @Override
    public BigDecimal aBigDecimal() {
        return oBigDecimal().orElse(null);
    }

    @Override
    public Optional<BigDecimal> oBigDecimal() {
        return oString().map(BigDecimal::new);
    }

    @Override
    public Boolean aBoolean() {
        return (Boolean)jsonValue;
    }

    @Override
    public Optional<Boolean> oBoolean() {
        return tryCatch(()->aBoolean());
    }

    @Override
    public boolean isNull() {
        return jsonValue == null;
    }

    @Override
    public List<JsonAst> list() {
        return (List<JsonAst>) 
                tryCatch(()->((JSONArray)jsonValue)
                        .stream()
                        .map(SimpleJsonAST::new)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        
    }

    //Use object() instead.  This may become private soon.
    @Override @Deprecated
    public Map<String, JsonAst> map() {
        JSONObject obj = ((JSONObject)jsonValue);
        return (Map<String, JsonAst>) 
                tryCatch(()->obj.keySet()
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), k->new SimpleJsonAST(obj.get(k)))))
                .orElse(Collections.emptyMap());
    }
    
    private static <T> Optional<T> tryCatch(Supplier<T> function){
        try{
            return Optional.ofNullable(function.get());
        }catch(Exception e){
            return Optional.empty();
        }
    }
    
    @Override
    public String toString(){
        return aString();
    }
    
}

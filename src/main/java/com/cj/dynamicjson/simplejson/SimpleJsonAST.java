package com.cj.dynamicjson.simplejson;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;

import javax.swing.text.html.Option;

public class SimpleJsonAST implements JsonAst{
 final Object jsonValue;
    
    SimpleJsonAST(Object jsonValue){
        this.jsonValue = jsonValue;
    }
    
    @Override
    public String aString() {
       return tryCatch(()->jsonValue.toString()).orElse(null);
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
        //instanceof is nasty, but no choice if we don't have type information
        if(jsonValue instanceof Boolean){
            if((Boolean) jsonValue) {
                return Optional.of(BigDecimal.ONE);
            } else {
                return Optional.of(BigDecimal.ZERO);
            }
        } else {
            try {
                return oString().map(BigDecimal::new);
            }catch(NumberFormatException e){
                return Optional.empty();
            }
        }
    }

    @Override
    public Boolean aBoolean() {
        //instanceof is nasty, but no choice if we don't have type information
        if(jsonValue == null) {
            return null;
        } else if(jsonValue instanceof Boolean) {
            return (Boolean)jsonValue;
        } else {
            return !(jsonValue.toString().equalsIgnoreCase("false") || jsonValue.toString().equalsIgnoreCase("0"));
        }
    }

    @Override
    public Optional<Boolean> oBoolean() {
        return tryCatch(this::aBoolean);
    }

    @Override
    public boolean isNull() {
        return jsonValue == null;
    }

    @Override
    public Stream<JsonAst> stream() {
        return (Stream<JsonAst>) 
                tryCatch(()->((JSONArray)jsonValue)
                        .stream()
                        .map(SimpleJsonAST::new))
                .orElse(Collections.emptyList().stream());
        
    }
    
    public List<JsonAst> list(){
        return stream().collect(Collectors.toList());
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

    @Override
    public Object internalAStringPrimitive() { return aString(); }
}

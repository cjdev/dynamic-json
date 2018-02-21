package com.cj.dynamicjson.jackson2;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;
import com.fasterxml.jackson.databind.JsonNode;

public class JacksonAst implements JsonAst{

	private final JsonNode rootNode;

	public JacksonAst(JsonNode node) {
		this.rootNode = node;
	}

    @Override
    public String aString() {
       return tryCatch(()->{
    	   		if(rootNode.isTextual()) return rootNode.textValue();
    	   		if(rootNode.isNull()) return null;
    	   		return rootNode.toString();
    	   		
    	   }).orElse(null);
    }

    @Override
    public Optional<String> oString() {
    		if(isNull()) return Optional.empty();
        return Optional.ofNullable(aString());
    }

    @Override
    public BigDecimal aBigDecimal() {
        return oBigDecimal().orElse(null);
    }

    @Override
    public Optional<BigDecimal> oBigDecimal() {
        //instanceof is nasty, but no choice if we don't have type information
        if(rootNode.isBoolean()){
            if(rootNode.asBoolean()) {
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
        if(isNull()) {
            return null;
        } else if(rootNode.isBoolean()) {
            return rootNode.asBoolean();
        } else {
            return !(rootNode.asText().equalsIgnoreCase("false") || rootNode.asText().equalsIgnoreCase("0"));
        }
    }

    @Override
    public Optional<Boolean> oBoolean() {
        return tryCatch(this::aBoolean);
    }

    @Override
    public boolean isNull() {
        return rootNode == null || rootNode.isNull();
    }

    private <T> Stream<T> stream(Iterator<T> iterator) {
    	return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),false);
    }
    
    @Override
    public Stream<JsonAst> stream() {
    		if (!rootNode.isArray()) return Collections.<JsonAst>emptyList().stream();
    		return stream(rootNode.elements()).map(JacksonAst::new);
    }
    
    public List<JsonAst> list(){
        return stream().collect(Collectors.toList());
    }

    //Use object() instead.  This may become private soon.
    @Override @Deprecated
    public Map<String, JsonAst> map() {
    		return stream(rootNode.fields()).collect(Collectors.toMap(Entry::getKey, entry->new JacksonAst(entry.getValue())));
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

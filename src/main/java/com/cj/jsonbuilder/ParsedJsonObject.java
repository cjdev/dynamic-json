package com.cj.jsonbuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.json.simple.JSONObject;

public class ParsedJsonObject {
	private final JSONObject internalObject;
	public ParsedJsonObject (JSONObject internalObject){
		this.internalObject = internalObject;
	}
	public Optional<String> getString(String key){
		return getObjectInternal(key).map(Object::toString);
	}
	
	public <T>Optional<T> get(String key, Function<String, T> mapper){
		return getString(key).map(mapper);
	}
	
	public Optional<Double> getDouble(String key){
		return get(key, Double::valueOf);
	}
	
	public Optional<Long> getLong(String key){
		return get(key, Long::valueOf);
	}
	
	public <T> List<T> getList(String key, Function<String, T> mapper){
		return getString(key).map(value->JsonParser.parseArray(value, mapper)).orElse(Collections.emptyList());
	}
	
	private Optional<Object> getObjectInternal(String key){
		if(internalObject==null) return Optional.empty();
		return Optional.ofNullable(internalObject.get(key));
	}
	
	
}
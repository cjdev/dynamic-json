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
		return get(key).map(Object::toString);
	}
	
	public Optional<Double> getDouble(String key){
		return getString(key).map(Double::valueOf);
	}
	
	public Optional<Long> getLong(String key){
		return getString(key).map(o->o.equals("") ? null : Long.valueOf(o));
	}
	
	public <T> List<T> getArray(String key, Function<String, T> mapper){
		return getString(key).map(value->JsonParser.parseArray(value, mapper)).orElse(Collections.emptyList());
	}
	
	private Optional<Object> get(String key){
		if(internalObject==null) return Optional.empty();
		return Optional.ofNullable(internalObject.get(key));
	}
	
	
}
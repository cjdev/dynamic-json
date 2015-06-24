package com.cj.jsonbuilder;

import java.util.Optional;

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
		return getString(key).map(Long::valueOf);
	}
	
	private Optional<Object> get(String key){
		if(internalObject==null) return Optional.empty();
		return Optional.ofNullable(internalObject.get(key));
	}
	
	
}
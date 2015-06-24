package com.cj.jsonbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JsonParser {
	public static <T> T parseObject(String json, Function<ParsedJsonObject, T> mapToJsonObject) {
		return mapToJsonObject.apply(new ParsedJsonObject((JSONObject)JSONValue.parse(json)));
	}
	
	public static <T> List<T>  parseArray(Optional<String> json, Function<Optional<String>, T> mapToDomain) {
		if(!json.isPresent()) return new ArrayList<T>();
		
		return parseArray(json.get(), mapToDomain);
	}
	public static <T> List<T>  parseArray(String json, Function<Optional<String>, T> mapToDomain) {
		JSONArray array = (JSONArray)JSONValue.parse(json);
		return (List<T>) array.stream().map(o->mapToDomain.apply(toStringEmptyIfNull(o))).collect(Collectors.toList());
	}
	
	private static Optional<String> toStringEmptyIfNull(Object input){
		return Optional.ofNullable(input).map(Object::toString);
	}
	
}

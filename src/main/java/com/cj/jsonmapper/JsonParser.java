package com.cj.jsonmapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JsonParser {
	public static <T> T parseObject(String json, Function<ParsedJsonObject, T> mapToJsonObject) {
		return mapToJsonObject.apply(new ParsedJsonObject((JSONObject)JSONValue.parse(json)));
	}
	
	public static <T> List<T> parseArrayOfObjects(String json, Function<ParsedJsonObject, T> mapToJsonObject){
		return parseArrayOptional(json, line->parseObject(line.get(), mapToJsonObject));
	}
	
	public static <T> Stream<ParsedJsonObject>  parseArrayOfObjects(String json){
		return parseArray(json, o->parseObject(o, Function.identity())).stream();
	}
	
	public static <T> List<T>  parseArray(Optional<String> json, Function<Optional<String>, T> mapToDomain) {
		if(!json.isPresent()) return new ArrayList<T>();
		
		return parseArrayOptional(json.get(), mapToDomain);
	}
	
	public static <T> List<T>  parseArray(String json, Function<String, T> mapToDomain) {
		JSONArray array = (JSONArray)JSONValue.parse(json);
		if (array==null) throw new JsonParseException("Can't Parse JSON: "+json);
		return (List<T>) array.stream().filter(o->o!=null).map(o->mapToDomain.apply(o.toString())).collect(Collectors.toList());
	}
	
	public static <T> List<T>  parseArrayOptional(String json, Function<Optional<String>, T> mapToDomain) {
		JSONArray array = (JSONArray)JSONValue.parse(json);
		if (array==null) throw new JsonParseException("Can't Parse JSON: "+json);
		return (List<T>) array.stream().map(o->mapToDomain.apply(toStringEmptyIfNull(o))).collect(Collectors.toList());
	}
	
	private static Optional<String> toStringEmptyIfNull(Object input){
		return Optional.ofNullable(input).map(Object::toString);
	}
	
}

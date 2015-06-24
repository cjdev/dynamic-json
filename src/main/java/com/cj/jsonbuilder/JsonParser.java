package com.cj.jsonbuilder;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JsonParser {
	public static <T> T parseObject(String json, Function<ParsedJsonObject, T> mapToJsonObject) {
		return mapToJsonObject.apply(new ParsedJsonObject((JSONObject)JSONValue.parse(json)));
	}
	public static <T> List<T>  parseArray(String json, Function<String, T> mapToDomain) {
		JSONArray array = (JSONArray)JSONValue.parse(json);
		return (List<T>) array.stream().map(o->mapToDomain.apply(o.toString())).collect(Collectors.toList());
	}
	
	
}

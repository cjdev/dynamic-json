package com.cj.jsonmapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonParser {


    public static <T> T object(String json, Function<ParsedJsonObject, T> mapToJsonObject) {
        return mapToJsonObject.apply(new ParsedJsonObject((JSONAware) JSONValue.parse(json)));
    }

    public static <T> Stream<ParsedJsonObject> objects(String json) {
        return array(json, o -> object(o, Function.identity())).stream();
    }

    public static <T> List<T> array(Optional<String> json, Function<Optional<String>, T> mapToDomain) {
        if (!json.isPresent()) return new ArrayList<T>();

        return arrayOptional(json.get(), mapToDomain);
    }

    public static <T> List<T> array(String json, Function<String, T> mapToDomain) {
        JSONArray array = (JSONArray) JSONValue.parse(json);
        if (array == null) throw new JsonParseException("Can't Parse JSON: " + json);
        return (List<T>) array.stream().filter(o -> o != null).map(o -> mapToDomain.apply(o.toString())).collect(Collectors.toList());
    }

    public static <T> List<T> arrayOptional(String json, Function<Optional<String>, T> mapToDomain) {
        JSONArray array = (JSONArray) JSONValue.parse(json);
        if (array == null) throw new JsonParseException("Can't Parse JSON: " + json);
        return (List<T>) array.stream().map(o -> mapToDomain.apply(toStringEmptyIfNull(o))).collect(Collectors.toList());
    }

    private static Optional<String> toStringEmptyIfNull(Object input) {
        return Optional.ofNullable(input).map(Object::toString);
    }

}

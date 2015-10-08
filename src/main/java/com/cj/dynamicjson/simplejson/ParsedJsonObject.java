package com.cj.dynamicjson.simplejson;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Deprecated
public class ParsedJsonObject {
    private final JSONAware internalObject;

    public ParsedJsonObject(JSONAware internalObject) {
        this.internalObject = internalObject;
    }

    public Optional<String> getString(String key) {
        return getObjectInternal(key).map(Object::toString);
    }

    public <T> Optional<T> get(String key, Function<String, T> mapper) {
        try {
            return getString(key).map(mapper);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Double> getDouble(String key) {
        return get(key, Double::valueOf);
    }

    public Optional<Long> getLong(String key) {
        return get(key, Long::valueOf);
    }

    public <T> List<T> getList(String key, Function<String, T> mapper) {
        return getString(key).map(value -> JsonParser.array(value, mapper)).orElse(Collections.emptyList());
    }

    public Optional<ParsedJsonObject> getObject(String key) {
        return get(key, o -> JsonParser.object(o, Function.identity()));
    }

    public Stream<String> strings() {
        return stream(Object::toString);
    }

    public Stream<Double> doubles() {
        return stream(o -> Double.parseDouble(o.toString()));
    }

    public Stream<Long> longs() {
        return stream(o -> Long.parseLong(o.toString()));
    }

    public Stream<ParsedJsonObject> getStream(String key) {
        return JsonParser.objects(getString(key).orElse("[]"));
    }

    public boolean isArray() {
        return internalObject instanceof JSONArray;
    }

    private Optional<JSONArray> internalObjectAsArray() {
        if (!isArray()) return Optional.empty();
        return Optional.ofNullable((JSONArray) internalObject);
    }


    public Stream<ParsedJsonObject> stream() {
        return stream(o -> new ParsedJsonObject((JSONAware) o));
    }

    private <T> Stream<T> stream(Function<Object, T> mapper) {
        if (isArray()) return (((JSONArray) internalObject).stream()).map(mapper);
        else throw new JsonParseException("JSON Doesn't Appear To Be An Array " + internalObject.toString());
    }

    private Optional<Object> getObjectInternal(String key) {
        if (internalObject == null) return Optional.empty();
        if (internalObject instanceof JSONObject) return Optional.ofNullable(((JSONObject) internalObject).get(key));
        return Optional.empty();
    }
}
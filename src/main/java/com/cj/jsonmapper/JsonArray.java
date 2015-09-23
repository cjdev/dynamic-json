package com.cj.jsonmapper;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;

public class JsonArray<T> {
    private JSONArray object = new JSONArray();

    private JsonArray() {
    }

    public static JsonArray<Number> ofNumbers(Collection<? extends Number> numbers) {
        return new JsonArray<Number>().withAllNumbers(numbers);
    }

    public static JsonArray<String> ofStrings(Collection<String> strings) {
        return new JsonArray<String>().withAllStrings(strings);
    }

    public static <T> JsonArray<T> ofArrays(Collection<T> objects, Function<T, JsonArray<T>> mapToJsonObject) {
        return new JsonArray<T>().withAll(objects.stream().map(mapToJsonObject).map(JsonArray::getInternalObject).collect(Collectors.toList()));
    }
    
    public static <T> JsonArray<T> of(Stream<T> objects, Function<T, JsonObject> mapToJsonObject) {
        return new JsonArray<T>().withAll(objects.map(mapToJsonObject).map(JsonObject::getInternalObject).collect(Collectors.toList()));
    }
    
    public static <T> JsonArray<T> of(Collection<T> objects, Function<T, JsonObject> mapToJsonObject) {
        return of(objects.stream(), mapToJsonObject);
    }

    public String toJson() {
        return object.toJSONString();
    }

    private JsonArray<T> withAll(Collection<JSONAware> objects) {
        object.addAll(objects);
        return this;
    }

    private JsonArray<T> withAllNumbers(Collection<? extends Number> objects) {
        if (objects == null) return this;
        object.addAll(removeNulls(objects));
        return this;
    }

    private JsonArray<T> withAllStrings(Collection<String> things) {
        object.addAll(removeNulls(things));
        return this;
    }

    private <T> Collection<T> removeNulls(Collection<T> c) {
        return c.stream().filter(o -> o != null).collect(Collectors.toList());
    }

    protected JSONArray getInternalObject() {
        return object;
    }

}
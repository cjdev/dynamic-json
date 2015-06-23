package com.cj.jsonbuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonArrayBuilder {
    private JSONArray object = new JSONArray();
    public JsonArrayBuilder with(JSONAware value){
        object.add(value);
        return this;
    }
    public JsonArrayBuilder with(String value){
        object.add(value);
        return this;
    }

    public JsonArrayBuilder withAll(Collection<JSONAware> objects) {
        object.addAll(objects);
        return this;
    }

    public static <T> JSONArray of(List<T> things, Function<T, JSONObject> mapToJsonObject){
        return new JsonArrayBuilder().withAll(things.stream().map(mapToJsonObject).collect(Collectors.toList())).build();
    }
    public JSONArray build(){
        return object;
    }
}
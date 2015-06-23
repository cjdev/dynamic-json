package com.cj.jsonbuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonArrayBuilder <T>{
    private JSONArray object = new JSONArray();
    public JsonArrayBuilder<T> with(JSONAware value){
        object.add(value);
        return this;
    }
    public JsonArrayBuilder<T> with(String value){
        object.add(value);
        return this;
    }

    public JsonArrayBuilder<T> withAll(Collection<JSONAware> objects) {
        object.addAll(objects);
        return this;
    }

    public static <T> JSONArray of(List<T> things, Function<T, JSONObject> mapToJsonObject){
        return new JsonArrayBuilder<T>().withAll(things.stream().map(mapToJsonObject).collect(Collectors.toList())).build();
    }
    public JSONArray build(){
        return object;
    }
    

}
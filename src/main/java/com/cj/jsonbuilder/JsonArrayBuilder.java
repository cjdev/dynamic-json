package com.cj.jsonbuilder;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class JsonArrayBuilder <T>{
    private JSONArray object = new JSONArray();
    private JsonArrayBuilder(){}
    private JsonArrayBuilder<T> with(JSONAware value){
        object.add(value);
        return this;
    }
    private JsonArrayBuilder<T> with(String value){
        object.add(value);
        return this;
    }

    private JsonArrayBuilder<T> withAll(Collection<JSONAware> objects) {
        object.addAll(objects);
        return this;
    }


    public static JSONArray ofNumbers(Collection<? extends Number> things){
        return new JsonArrayBuilder<Number>().withAllNumbers(things).build();
    }

    public static JSONArray ofStrings(Collection<String> things){
        return new JsonArrayBuilder<Number>().withAllStrings(things).build();
    }



    public static <T> JSONArray of(Collection<T> things, Function<T, JSONObject> mapToJsonObject){
        return new JsonArrayBuilder<T>().withAll(things.stream().map(mapToJsonObject).collect(Collectors.toList())).build();
    }

    public JSONArray build(){
        return object;
    }



    private JsonArrayBuilder withAllNumbers(Collection<? extends Number> objects) {
    	if(objects == null) return this;
        object.addAll(removeNulls(objects));
        return this;
    }
    private JsonArrayBuilder withAllStrings(Collection<String> things) {
        object.addAll(removeNulls(things));
        return this;
    }

    private <T> Collection<T> removeNulls(Collection<T> c){
        return c.stream().filter(o->o!=null).collect(Collectors.toList());
    }

}
package com.cj.jsonbuilder;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class JsonArrayFactory <T>{
    private JSONArray object = new JSONArray();
    private JsonArrayFactory(){}	

    public static JSONArray ofNumbers(Collection<? extends Number> numbers){
        return new JsonArrayFactory<Number>().withAllNumbers(numbers).build();
    }

    public static JSONArray ofStrings(Collection<String> strings){
        return new JsonArrayFactory<Number>().withAllStrings(strings).build();
    }

    public static <T> JSONArray of(Collection<T> objects, Function<T, JSONObject> mapToJsonObject){
        return new JsonArrayFactory<T>().withAll(objects.stream().map(mapToJsonObject).collect(Collectors.toList())).build();
    }

    private JSONArray build(){
        return object;
    }

    private JsonArrayFactory<T> withAll(Collection<JSONAware> objects) {
        object.addAll(objects);
        return this;
    }
    private JsonArrayFactory withAllNumbers(Collection<? extends Number> objects) {
    	if(objects == null) return this;
        object.addAll(removeNulls(objects));
        return this;
    }
    private JsonArrayFactory withAllStrings(Collection<String> things) {
        object.addAll(removeNulls(things));
        return this;
    }
    private <T> Collection<T> removeNulls(Collection<T> c){
        return c.stream().filter(o->o!=null).collect(Collectors.toList());
    }

}
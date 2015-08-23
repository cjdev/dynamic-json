package com.cj.jsonmapper;

import org.json.simple.JSONObject;

public class JsonObjectBuilder{
    private JSONObject object = new JSONObject();
    public JsonObjectBuilder with(String key, String value){
        if(value ==null) return this;
        object.put(key, value);
        return this;
    }
    public JsonObjectBuilder with(String key, JsonObjectBuilder value){
    	if(value ==null) return this;
        object.put(key, value.getInternalObject());
        return this;
    }

    public JsonObjectBuilder with(String key, JsonArray value){
    	if(value ==null) return this;
        object.put(key, value.getInternalObject());
        return this;
    }
    
    public JsonObjectBuilder withAsString(String key, Object value){
        if(value ==null) return this;
        return  with(key, value.toString());
    }
    
    public String toJson(){
    	return object.toJSONString();
    }

    protected JSONObject getInternalObject(){
        return object;
    }
    

}

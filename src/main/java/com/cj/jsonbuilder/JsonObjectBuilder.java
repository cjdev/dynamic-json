package com.cj.jsonbuilder;



import org.json.simple.JSONAware;
import org.json.simple.JSONObject;


public class JsonObjectBuilder{
    private JSONObject object = new JSONObject();
    public JsonObjectBuilder with(String key, String value){
        object.put(key, value);
        return this;
    }
    public JsonObjectBuilder with(String key, JSONAware value){
        object.put(key, value);
        return this;
    }

    public JsonObjectBuilder withAsString(String key, Object value){
        return with(key, value.toString());
    }

    public JSONObject build(){
        return object;
    }
    

}

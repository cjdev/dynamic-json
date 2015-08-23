package com.cj.jsonbuilder;



import org.json.simple.JSONAware;
import org.json.simple.JSONObject;


public class JsonObjectBuilder{
    private JSONObject object = new JSONObject();
    public JsonObjectBuilder with(String key, String value){
        if(value ==null) return this;
        object.put(key, value);
        return this;
    }
    public JsonObjectBuilder with(String key, JSONAware value){
    	if(value ==null) return this;
        object.put(key, value);
        return this;
    }

    public JsonObjectBuilder withAsString(String key, Object value){
        if(value ==null) return this;
        return  with(key, value.toString());
    }
    
    public String toJson(){
    	return object.toJSONString();
    }

    @Deprecated //Leaky Abstraction
    public JSONObject build(){
        return object;
    }
    

}

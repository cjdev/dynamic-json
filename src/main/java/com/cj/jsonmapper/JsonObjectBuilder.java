package com.cj.jsonmapper;

import org.json.simple.JSONObject;

public class JsonObjectBuilder{
    private JSONObject object = new JSONObject();
    
    public JsonObjectBuilder(){}
    
    public JsonObjectBuilder with(String key, String value){
        return append(key, value);
    }
    
	public JsonObjectBuilder with(String key, JsonObjectBuilder value){
    	return append(key, value.getInternalObject());
    }

    public JsonObjectBuilder with(String key, JsonArray value){
    	if(value == null) return this;
    	return append(key, value.getInternalObject());
    }
    
    public JsonObjectBuilder withAsString(String key, Object value){
        if(value == null) return this;
        return  with(key, value.toString());
    }
    
    public String toJson(){
    	return object.toJSONString();
    }

    protected JSONObject getInternalObject(){
        return object;
    }
    
    private JsonObjectBuilder append(String key, Object value){
    	JSONObject newObject = new JSONObject();
    	newObject.putAll(this.object);
    	if(value!=null) newObject.put(key, value);
    	return new JsonObjectBuilder(newObject);
    }
    private JsonObjectBuilder(JSONObject state) {
    	this();
		this.object=state;
	}
    

}

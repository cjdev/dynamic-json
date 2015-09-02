package com.cj.jsonmapper;

import org.json.simple.JSONObject;

public class JsonObject {
    private JSONObject object = new JSONObject();

    public JsonObject() {
    }

    public JsonObject with(String key, String value) {
        return append(key, value);
    }

    public JsonObject with(String key, JsonObject value) {
        return append(key, value.getInternalObject());
    }

    public JsonObject with(String key, JsonArray value) {
        if (value == null) return this;
        return append(key, value.getInternalObject());
    }

    public JsonObject withAsString(String key, Object value) {
        if (value == null) return this;
        return with(key, value.toString());
    }

    public String toJson() {
        return object.toJSONString();
    }

    protected JSONObject getInternalObject() {
        return object;
    }

    private JsonObject append(String key, Object value) {
        JSONObject newObject = new JSONObject();
        newObject.putAll(this.object);
        if (value != null) newObject.put(key, value);
        return new JsonObject(newObject);
    }

    private JsonObject(JSONObject state) {
        this();
        this.object = state;
    }


}

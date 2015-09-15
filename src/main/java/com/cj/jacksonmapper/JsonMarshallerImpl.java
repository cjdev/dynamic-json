package com.cj.jacksonmapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

public class JsonMarshallerImpl implements JsonMarshaller {
    private static JsonMarshaller INSTANCE = new JsonMarshallerImpl();

    public static JsonMarshaller instance() {
        return INSTANCE;
    }

    private ObjectMapper mapperDelegate = new ObjectMapper();
    private ObjectMapperWithoutCheckedExceptions mapper = new ObjectMapperWithoutCheckedExceptions(new ObjectMapper());

    public JsonMarshallerImpl() {
        mapperDelegate.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapperDelegate.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapperDelegate.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @Override
    public String toJson(Object theObject) {
        String json = mapper.writeValueAsString(theObject);
        return json;
    }

    @Override
    public <T> T fromJson(String json, Class<T> theClass) {
        T parsed = mapper.readValue(json, theClass);
        return parsed;
    }

    @Override
    public <T> List<T> fromJsonArray(String json, Class<T> theElementClass) {
        CollectionType collectionType = mapperDelegate.getTypeFactory().constructCollectionType(List.class, theElementClass);
        List<T> myObjects = mapper.readValue(json, collectionType);
        return myObjects;
    }

    @Override
    public String normalize(String json) {
        Object value = mapper.readValue(json, Object.class);
        String valueAsString = mapper.writeValueAsString(value);
        return valueAsString;
    }
}

package com.cj.jacksonmapper;

import java.util.List;

public interface JsonMarshaller {
    String toJson(Object theObject);

    <T> T fromJson(String json, Class<T> theClass);

    <T> List<T> fromJsonArray(String json, Class<T> theElementClass);

    String normalize(String json);
}

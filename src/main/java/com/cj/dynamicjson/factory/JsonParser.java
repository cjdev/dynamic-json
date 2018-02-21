package com.cj.dynamicjson.factory;

import java.io.InputStream;

import com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;
import com.cj.dynamicjson.jackson2.Marshaller;

public class JsonParser {
    public static JsonAst parse(String json){return Marshaller.instance.parse(json);}
    public static JsonAst parse(InputStream json){return Marshaller.instance.parse(json);}
}

package com.cj.dynamicjson;

import java.io.InputStream;

import com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;
import com.cj.dynamicjson.simplejson.Marshaller;

public class JsonParser {
    public static JsonAst parse(String json){return Marshaller.instance.parse(json);}
    public static JsonAst parse(InputStream json){return Marshaller.instance.parse(json);}
}

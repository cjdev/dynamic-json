package com.cj.dynamicjson;

import java.io.InputStream;

import static com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;

public interface Marshaller {
    JsonAst parse(String jsonText);

    JsonAst parse(InputStream jsonText);
}

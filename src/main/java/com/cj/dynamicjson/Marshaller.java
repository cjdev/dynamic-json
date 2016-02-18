package com.cj.dynamicjson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;

public interface Marshaller {
    JsonAst parse(String jsonText);

    JsonAst parse(InputStream jsonText);

    default Stream<AbstractSyntaxTree.JsonAst> parseList(String jsonText) {

        JsonFactory f = new MappingJsonFactory();

        try {
            // using Jackson to stream the parsing since it's better at it than simple JSONParser
            com.fasterxml.jackson.core.JsonParser jp = f.createParser(jsonText);
            JsonToken current = jp.nextToken();

            if(current != JsonToken.START_ARRAY) {
                throw new RuntimeException("bad json");
            }

            Supplier<Boolean> hasNext = () -> {
                return Try.to(()->jp.nextToken() != JsonToken.END_ARRAY).orElseThrow(()->new RuntimeException("Error Parsing Json"));
            };

            Supplier<JsonAst> next = () -> {
                return Try.to(()->{
                    JsonNode node = jp.readValueAsTree();
                    return parse(node.toString());
                }).orElseThrow(()->new RuntimeException("Error Parsing Json"));
            };

            IteratorImpl<JsonAst> iterator = new IteratorImpl<>(next, hasNext);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}

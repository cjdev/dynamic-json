package com.cj.dynamicjson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Spliterator;
import java.util.Spliterators;
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

    //TODO: This needs a test.
    default Stream<AbstractSyntaxTree.JsonAst> parseList(String jsonText) {

        JsonFactory f = new MappingJsonFactory();

        try {
            // using Jackson to stream the parsing since it's better at it than simple JSONParser
            com.fasterxml.jackson.core.JsonParser jp = f.createParser(jsonText);
            JsonToken current = jp.nextToken();

            if(current != JsonToken.START_ARRAY) {
                throw new RuntimeException("bad json");
            }

            IteratorImpl<JsonAst> iterator = new IteratorImpl<>(
                    () -> {
                        JsonNode node = jp.readValueAsTree();
                        jp.nextToken(); //Move to the next tree.
                        return parse(node.toString());  
                    }, () -> {
                        return jp.getCurrentToken() != JsonToken.END_ARRAY; 
                    });
            
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}

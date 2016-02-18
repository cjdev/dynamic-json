package com.cj.dynamicjson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
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

    /**
     * While the other parse methods in this class will parse JSON lists, this one will only process one element of the list at a time
     * to prevent consuming too much memory when the input is large.
     * @param json
     * @return
     */
    default Stream<JsonAst> parseList(InputStream json) {

        JsonFactory f = new MappingJsonFactory();

        try {
            // using Jackson to stream the parsing since it's better at it than simple JSONParser
            com.fasterxml.jackson.core.JsonParser jp = f.createParser(json);
            JsonToken current = jp.nextToken();

            if(current != JsonToken.START_ARRAY) {
                throw new RuntimeException("bad json");
            }
            
            jp.nextToken();

            return new IteratorBuilder<JsonAst>()
                .withNext(()->{
                    JsonNode node = jp.readValueAsTree();
                    jp.nextToken(); //Move to the next tree.
                    return parse(node.toString());  
                    })
                .withHasNext(() -> {
                    return jp.getCurrentToken() !=null && jp.getCurrentToken() != JsonToken.END_ARRAY; 
                    })
                .stream();

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}

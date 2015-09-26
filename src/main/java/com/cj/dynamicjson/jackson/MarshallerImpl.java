package com.cj.dynamicjson.jackson;

import com.cj.dynamicjson.Marshaller;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cj.dynamicjson.AbstractSyntaxTree.*;

public class MarshallerImpl implements Marshaller {
    public static final MarshallerImpl instance = new MarshallerImpl();

    private MarshallerImpl() {
    }

    private UncheckedJsonFactory factory = new UncheckedJsonFactory(new JsonFactory());

    @Override
    public JsonAst parse(String jsonText) {
        return parse(factory.createParser(jsonText));
    }

    @Override
    public JsonAst parse(InputStream jsonText) {
        return parse(factory.createParser(jsonText));
    }

    private JsonAst parse(UncheckedJsonParser parser) {
        parser.nextToken();
        return consumeJson(parser);
    }

    private JsonAst consumeJson(UncheckedJsonParser parser) {
        JsonToken token = parser.delegate.getCurrentToken();
        JsonAst result;
        if (token == JsonToken.VALUE_STRING) {
            result = new JsonString(parser.getText());
            consumeToken(parser, JsonToken.VALUE_STRING);
        } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
            result = new JsonNumber(parser.getDecimalValue());
            consumeToken(parser, JsonToken.VALUE_NUMBER_FLOAT);
        } else if (token == JsonToken.VALUE_TRUE) {
            result = new JsonBoolean(true);
            consumeToken(parser, JsonToken.VALUE_TRUE);
        } else if (token == JsonToken.VALUE_FALSE) {
            result = new JsonBoolean(false);
            consumeToken(parser, JsonToken.VALUE_FALSE);
        } else if (token == JsonToken.VALUE_NULL) {
            result = JsonNull.instance;
            consumeToken(parser, JsonToken.VALUE_NULL);
        } else if (token == JsonToken.START_ARRAY) {
            consumeToken(parser, JsonToken.START_ARRAY);
            JsonArray array = consumeArrayContents(parser);
            consumeToken(parser, JsonToken.END_ARRAY);
            result = array;
        } else if (token == JsonToken.START_OBJECT) {
            consumeToken(parser, JsonToken.START_OBJECT);
            JsonObject object = consumeObjectContents(parser);
            consumeToken(parser, JsonToken.END_OBJECT);
            result = object;
        } else {
            throw new RuntimeException(String.format("Unexpected token %s", token));
        }
        return result;
    }

    private void consumeToken(UncheckedJsonParser parser, JsonToken token) {
        expectToken(parser, token);
        parser.nextToken();
    }

    private JsonArray consumeArrayContents(UncheckedJsonParser parser) {
        List<JsonAst> array = new ArrayList<>();
        while (parser.delegate.getCurrentToken() != JsonToken.END_ARRAY) {
            array.add(consumeJson(parser));
        }
        return new JsonArray(array);
    }

    private JsonObject consumeObjectContents(UncheckedJsonParser parser) {
        Map<String, JsonAst> object = new HashMap<>();
        while (parser.delegate.getCurrentToken() != JsonToken.END_OBJECT) {
            String key = consumeKey(parser);
            JsonAst value = consumeJson(parser);
            object.put(key, value);
        }
        return new JsonObject(object);
    }

    private String consumeKey(UncheckedJsonParser parser) {
        expectToken(parser, JsonToken.FIELD_NAME);
        String result = parser.getCurrentName();
        parser.nextToken();
        return result;
    }

    private void expectToken(UncheckedJsonParser parser, JsonToken expected) {
        JsonToken actual = parser.delegate.getCurrentToken();
        if (actual != expected) {
            throw new RuntimeException(String.format("Expected token %s, but got %s instead.", expected, actual));
        }
    }
}

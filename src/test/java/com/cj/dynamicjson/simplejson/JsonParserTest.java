package com.cj.dynamicjson.simplejson;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Deprecated
public class JsonParserTest {
    @Test
    public void testEmptyListHydrationHandling() {
        String jsonString = "[]";
        List<String> result = JsonParser.array(jsonString, String::toString);

        assertEquals(0, result.size());
    }

    @Test
    public void testNullListHydrationHandlingOptional() {
        String jsonString = "[null]";
        List<Optional<String>> result = JsonParser.arrayOptional(jsonString, o -> o);

        assertEquals(1, result.size());
        assertEquals(Optional.empty(), result.get(0));
    }

    @Test
    public void testNullListHydrationHandling() {
        String jsonString = "[null]";
        List<String> result = JsonParser.array(jsonString, o -> o);

        assertEquals(0, result.size());
    }

    @Test
    public void primitiveParsing() {
        String jsonStringWithLong = "{\"key\":1}";
        String jsonStringWithNull = "{\"key\":null}";
        String emptyString = "{}";
        String jsonString = "{\"key\":\"1\"}";
        assertEquals(Long.valueOf(-1), JsonParser.<Long>object(jsonStringWithNull, o -> o.getLong("key").orElse(-1l)));
        assertEquals(Long.valueOf(1), JsonParser.<Long>object(jsonStringWithLong, o -> o.getLong("key").get()));
        assertEquals(Long.valueOf(1), JsonParser.<Long>object(jsonString, o -> o.getLong("key").get()));
        assertEquals(Optional.empty(), JsonParser.<Optional<Long>>object(emptyString, o -> o.getLong("key")));
    }

    @Test
    public void parseEmptyStringIntoEmptyLong() {
        String jsonString = "{\"key\":\"\"}";

        assertFalse("An empty string representing a long should be parsed into an empty optional",
                JsonParser.object(jsonString, o -> o.getLong("key").isPresent()));
    }

    @Test
    public void testSimpleUnJsonification() {
        String arrayString = "[{\"field1\":\"Field1\",\"field2\":\"Field2\"}]";
        List<UninterestingObject> obj = JsonParser.arrayOptional(arrayString, arrayElement ->
                        JsonParser.object(arrayElement.orElse(null), element ->
                                        new UninterestingObject(element.getString("field1").orElse(null), element.getString("field2").orElse(null))
                        )
        );

        assertEquals(obj.get(0).field1, "Field1");
        assertEquals(obj.get(0).field2, "Field2");
    }

    @Test(expected = JsonParseException.class)
    public void unparsableJsonShouldThrowAUsefulException() {
        String badJson = "[[";
        JsonParser.array(badJson, o -> "String");
    }

    @Test
    public void canParseAnArrayOfArrays() {
        String arrayOfArrays = "[[[\"a1\"],[\"a2\"]],[[\"b1\"],[\"b2\"]]]";
        List<String> result = JsonParser.objects(arrayOfArrays).flatMap(ParsedJsonObject::stream).flatMap(ParsedJsonObject::strings).collect(Collectors.toList());

        assertTrue(result.contains("a1"));
    }

    class UninterestingObject {
        public final String field1;
        public final String field2;

        public UninterestingObject(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }

}
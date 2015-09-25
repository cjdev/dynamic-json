package com.cj.jsonmapper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.cj.jsonmapper.TestHelper.loadResourceAsString;
import static com.cj.jsonmapper.TestHelper.normalize;
import static org.junit.Assert.assertEquals;

public class JsonBuilderTest {
    @Test
    public void testSimpleJsoninification() {
        List<UninterestingObject> uninterestingObjects = new ArrayList<UninterestingObject>();
        uninterestingObjects.add(new UninterestingObject("Field1", "Field2"));

        String actual = JsonArray.of(uninterestingObjects, (object) ->
                        new JsonObject()
                                .with("field1", object.field1)
                                .with("field2", object.field2)
        ).toJson();

        String expected = loadResourceAsString("array-of-simple-map.json");
        assertEquals(normalize(expected), normalize(actual));
    }

    @Test
    public void testNullDehydrationHandling() {
        String jsonString = new JsonObject()
                .with("field1", "null")
                .with("field2", (String) null)
                .getInternalObject().toJSONString();
        assertEquals("{\"field1\":\"null\"}", jsonString);

        jsonString = new JsonObject()
                .withAsString("field1", null)
                .withAsString("field2", "null")
                .getInternalObject().toJSONString();
        assertEquals("{\"field2\":\"null\"}", jsonString);

        assertEquals("[]", JsonArray.ofNumbers(null).toJson());
    }

    @Test
    public void testNumbersAndStrings() {
        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(null);
        numbers.add(345);
        String jsonString = JsonArray.ofNumbers(numbers).toJson();

        assertEquals("[345]", jsonString);

        List<String> strings = new ArrayList<String>();
        strings.add(null);
        strings.add("string");
        jsonString = JsonArray.ofStrings(strings).toJson();

        assertEquals("[\"string\"]", jsonString);

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

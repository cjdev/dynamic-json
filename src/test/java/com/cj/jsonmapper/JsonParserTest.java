package com.cj.jsonmapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.cj.jsonmapper.JsonParser;
import com.cj.jsonmapper.JsonBuilderTest.UninterestingObject;

public class JsonParserTest {
    @Test
    public void testEmptyListHydrationHandling(){
        String jsonString = "[]";
		List<String> result = JsonParser.parseArray(jsonString, o->o.toString());
		
        assertEquals(0, result.size());
    }
    
    @Test
    public void testNullListHydrationHandlingOptional(){
        String jsonString = "[null]";
		List<Optional<String>> result = JsonParser.parseArrayOptional(jsonString, o->o);
		
        assertEquals(1, result.size());
        assertEquals(Optional.empty(), result.get(0));
    }
    
    @Test
    public void testNullListHydrationHandling(){
        String jsonString = "[null]";
		List<String> result = JsonParser.parseArray(jsonString, o->o);
		
        assertEquals(0, result.size());
    }
    
    @Test
    public void primitiveParsing(){
    	String jsonString = "{\"key\":1}";
    	String jsonStringWithNull = "{\"key\":null}";
    	String emptyString = "{}";
    	assertEquals(Long.valueOf(-1), JsonParser.<Long>parseObject(jsonStringWithNull, o->o.getLong("key").orElse(-1l)));
    	assertEquals(Long.valueOf(1), JsonParser.<Long>parseObject(jsonString, o->o.getLong("key").get()));
    	assertEquals(Optional.empty(), JsonParser.<Optional<Long>>parseObject(emptyString, o->o.getLong("key")));
    }

    @Test
    public void parseEmptyStringIntoEmptyLong() {
        String jsonString = "{\"key\":\"\"}";

        assertFalse("An empty string representing a long should be parsed into an empty optional",
                JsonParser.parseObject(jsonString, o -> o.getLong("key").isPresent()));
    }
    
    @Test
    public void testSimpleUnJsonification(){
    	String arrayString = "[{\"field1\":\"Field1\",\"field2\":\"Field2\"}]";
    	List<UninterestingObject> obj = JsonParser.parseArrayOptional(arrayString, arrayElement ->
                        JsonParser.parseObject(arrayElement.orElse(null), element ->
                                        new UninterestingObject(element.getString("field1").orElse(null), element.getString("field2").orElse(null))
                        )
        );
    	
    	assertEquals(obj.get(0).field1, "Field1");
    	assertEquals(obj.get(0).field2, "Field2");
    }
    
    @Test(expected=JsonParseException.class)
    public void unparsableJsonShouldThrowAUsefulException(){
    	String badJson = "[[";
    	JsonParser.parseArray(badJson, o->"String");
    }
    
    class UninterestingObject{
        public final String field1;
        public final String field2;
        public UninterestingObject(String field1, String field2){
        	this.field1 = field1;
        	this.field2 = field2;
        }
    }

}

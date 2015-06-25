package com.cj.jsonbuilder;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

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
    
    class UninterestingObject{
        public final String field1;
        public final String field2;
        public UninterestingObject(String field1, String field2){
        	this.field1 = field1;
        	this.field2 = field2;
        }
    }

}

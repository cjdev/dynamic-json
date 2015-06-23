package com.cj.jsonbuilder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonBuilderTest {
    @Test
    public void testSimpleJsoninification(){
        List<UninterestingObject> uninterestingObjects = new ArrayList<UninterestingObject>();
        uninterestingObjects.add(new UninterestingObject("Field1", "Field2"));
        
        String jsonString = JsonArrayBuilder.of(uninterestingObjects, (object) ->
            new JsonObjectBuilder()
                    .with("field1", object.field1)
                    .with("field2", object.field2)
                    .build()).toJSONString();
                    
        assertEquals("[{\"field1\":\"Field1\",\"field2\":\"Field2\"}]", jsonString);
    }
    
    @Test
    public void testSimpleUnJsonification(){
    	String arrayString = "[{\"field1\":\"Field1\",\"field2\":\"Field2\"}]";
    	List<UninterestingObject> obj = JsonParser.<UninterestingObject>parseArray(arrayString, arrayElement ->
    	JsonParser.<UninterestingObject>parseObject(arrayElement, element->
    			new UninterestingObject(element.get("field1").toString(), element.get("field2").toString())
    		)
    	);
    	
    	assertEquals(obj.get(0).field1, "Field1");
    	assertEquals(obj.get(0).field2, "Field2");
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

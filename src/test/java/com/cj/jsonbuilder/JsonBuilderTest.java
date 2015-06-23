package com.cj.jsonbuilder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonBuilderTest {
    @Test
    public void testSimpleJsoninification(){
        List<UninterestingObject> uninterestingObjects = new ArrayList<UninterestingObject>();
        uninterestingObjects.add(new UninterestingObject());
        
        String jsonString = JsonArrayBuilder.of(uninterestingObjects, (object) ->
            new JsonObjectBuilder()
                    .with("field1", object.field1)
                    .with("field2", object.field2)
                    .build()).toJSONString();
                    
        assertEquals("[{\"field1\":\"Field1\",\"field2\":\"Field2\"}]", jsonString);           
    }
    

    
    class UninterestingObject{
        public final String field1="Field1";
        public final String field2="Field2";
    }
}

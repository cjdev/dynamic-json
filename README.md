# dynamic-json
Easy JSON parsing with a liberal application of Java Streams and Optionals.  Here's an example:
'''java
@Test
public void aSimpleExample(){
    String json = "{\"name\":\"Abraham\", \"children\":[\"Robert\", \"Edward\", \"William\", \"Thomas\"]}";
    JsonObject person = JsonParser.parse(json).object();
    assertEquals("Abraham", person.get("name").aString());
    
    List<String> children = person.get("children").listOf(JsonAst::aString);
    
    assertEquals("Robert", children.get(0));
    assertEquals("Edward", children.get(1));
    assertEquals("William", children.get(2));
    assertEquals("Thomas", children.get(3));
    
}
'''

More examples can be found in the <a href='https://github.com/cjdev/dynamic-json/blob/master/src/test/java/com/cj/dynamicjson/simplejson/Examples.java'>Examples.java source file</a>.


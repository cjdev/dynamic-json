# dynamic-json
Easy JSON parsing with a liberal application of Java Streams and Optionals.  

<H1>Basic Example</H1>
```java
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
```

<H1>Querying Json</H1>
Given the following json, let's suppose we'd like to find all of the titles of books written by Jim Houndface:
```json
[
  {
    "title": "Dog Lovers",
    "authors": [
      "Arnold Sweitcher",
      "Jim Houndface"
    ],
    "price": "5.65"
  },
  {
    "title": "Cat Lovers",
    "authors": [
      "David Munchkin",
      "Jim Houndface"
    ],
    "price": "5.99"
  },
  {
    "title": "Webster's Dictionary",
    "authors": [
      "Webster"
    ],
    "price": "12.00"
  }
]
```
The solution:
```java
@Test
public void findAllBooksWrittenByJim() {
  List<String> booksByJim =  JsonParser.parse(books).stream()
      .filter(book -> book.object().get("authors").listOf(JsonAst::toString).contains("Jim Houndface"))
      .map(book -> book.object().get("title").oString().orElse("No Title Found"))
      .collect(Collectors.toList());
    
  assertTrue(booksByJim.contains("Dog Lovers"));
  assertTrue(booksByJim.contains("Cat Lovers"));
}
```


More examples can be found in the <a href='https://github.com/cjdev/dynamic-json/blob/master/src/test/java/com/cj/dynamicjson/simplejson/Examples.java'>Examples.java source file</a>.


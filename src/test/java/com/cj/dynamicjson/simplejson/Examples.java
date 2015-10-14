package com.cj.dynamicjson.simplejson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;
import com.cj.dynamicjson.AbstractSyntaxTree.JsonObject;
import com.cj.dynamicjson.JsonParser;

public class Examples {
    final String books = TestHelper.loadResourceAsString("books-sample.json");

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
    
    @Test
    public void findCheapestBookStream() {
      Optional<Double> price = JsonParser.parse(books).list().stream()
              .map(book -> book.object().oGet("price").flatMap(JsonAst::oBigDecimal).orElse(BigDecimal.valueOf(Double.MAX_VALUE)))
              .min(BigDecimal::compareTo)
              .map(BigDecimal::doubleValue);

        assertEquals(price, Optional.of(5.65));
    }

    @Test
    public void findAllBooksWrittenByJim() {
        List<String> booksByJim =  JsonParser.parse(books).stream()
              .filter(book -> book.object().get("authors").listOf(JsonAst::toString).contains("Jim Houndface"))
              .map(book -> book.object().get("title").oString().orElse("No Title Found"))
              .collect(Collectors.toList());
        
        assertTrue(booksByJim.contains("Dog Lovers"));
        assertTrue(booksByJim.contains("Cat Lovers"));
    }

    @Test
    public void toDto() {
        List<Book> bookObjects =  JsonParser.parse(books).listOf(Book::jsonToBook);
        assertEquals(3, bookObjects.size());
        assertEquals(2, bookObjects.get(1).authors.size());
    }

    static class Book {
        public final List<String> authors;
        public final BigDecimal price;
        public final String title;
        public Book(List<String> authors, BigDecimal price, String title){
            this.authors = authors;
            this.price = price;
            this.title = title;
        }
        
        public static Book jsonToBook(JsonAst json){
            JsonObject book = json.object();
            BigDecimal price = book.oGet("price").flatMap(JsonAst::oBigDecimal).orElse(BigDecimal.valueOf(Double.MAX_VALUE));
            String title = book.oGet("Title").flatMap(JsonAst::oString).orElse("No Title Found");
            List<String> authors = book.oGet("authors").map(e->e.listOf(JsonAst::toString)).orElse(Collections.emptyList());
            return new Book(authors, price, title);
        }
        
    }
}

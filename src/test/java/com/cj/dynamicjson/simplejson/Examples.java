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

public class Examples {
    Function<JsonAst, Book> mapper = bookRow -> {
        Book bookToReturn = new Book();
        JsonObject book = bookRow.object();
        bookToReturn.price = book.oGet("price").flatMap(JsonAst::oBigDecimal).orElse(BigDecimal.valueOf(Double.MAX_VALUE));
        bookToReturn.title = book.oGet("Title").flatMap(JsonAst::oString).orElse("No Title Found");
        bookToReturn.authors = book.oGet("authors").map(e->e.list().stream().map(JsonAst::toString).collect(Collectors.toList())).orElse(Collections.emptyList());
        return bookToReturn;
    };

    final String books = TestHelper.loadResourceAsString("books-sample.json");

    @Test
    public void findCheapestBookStream() {
      Optional<Double> price = new MarshallerImpl().parse(books).list().stream()
              .map(book -> book.object().oGet("price").flatMap(JsonAst::oBigDecimal).orElse(BigDecimal.valueOf(Double.MAX_VALUE)))
              .min(BigDecimal::compareTo)
              .map(BigDecimal::doubleValue);

        assertEquals(price, Optional.of(5.65));
    }

    @Test
    public void findAllBooksWrittenByJim() {
        List<String> booksByJim =  new MarshallerImpl().parse(books).list().stream()
              .filter(book -> book.object().get("authors").list().stream().map(JsonAst::toString).collect(Collectors.toList()).contains("Jim Houndface"))
              .map(book -> book.object().get("title").oString().orElse("No Title Found"))
              .collect(Collectors.toList());
        
        assertTrue(booksByJim.contains("Dog Lovers"));
        assertTrue(booksByJim.contains("Cat Lovers"));
    }

    @Test
    public void toDto() {
        List<Book> bookObjects =  new MarshallerImpl().parse(books).list().stream().map(mapper).collect(Collectors.toList());
        assertEquals(3, bookObjects.size());
        assertEquals(2, bookObjects.get(1).authors.size());
    }

    class Book {
        public List<String> authors;
        public BigDecimal price;
        public String title;
    }

}

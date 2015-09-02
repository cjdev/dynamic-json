package com.cj.jsonmapper;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Examples {
    Function<ParsedJsonObject, Book> mapper = bookRow -> {
        Book bookToReturn = new Book();
        bookToReturn.price = BigDecimal.valueOf(bookRow.getDouble("price").orElse(Double.MAX_VALUE));
        bookToReturn.title = bookRow.getString("Title").orElse("No Title Found");
        bookToReturn.authors = bookRow.getList("authors", String::toString);
        return bookToReturn;
    };

    final String books = TestHelper.loadResourceAsString("books-sample.json");

    @Test
    public void findCheapestBookStream() {
        Optional<Double> price = JsonParser.objects(books).map(book -> book.getDouble("price").orElse(Double.MAX_VALUE))
                .min(Double::compareTo);
        assertEquals(price, Optional.of(5.65));
    }

    @Test
    public void findAllBooksWrittenByJim() {
        List<String> booksByJim = JsonParser.objects(books)
                .filter(book -> book.getList("authors", String::toString).contains("Jim Houndface"))
                .map(book -> book.getString("title").orElse("No Title Found"))
                .collect(Collectors.toList());
        assertTrue(booksByJim.contains("Dog Lovers"));
        assertTrue(booksByJim.contains("Cat Lovers"));
    }

    @Test
    public void toDto() {
        List<Book> bookObjects = JsonParser.objects(books).map(mapper).collect(Collectors.toList());
        assertEquals(3, bookObjects.size());
        assertEquals(2, bookObjects.get(1).authors.size());
    }

    class Book {
        public List<String> authors;
        public BigDecimal price;
        public String title;
    }

}

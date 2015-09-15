package com.cj.jacksonmapper;

import com.cj.jsonmapper.JsonParser;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Examples {
    JsonMarshaller jsonMarshaller = JsonMarshallerImpl.instance();
    String booksJson = TestHelper.loadResourceAsString("books-sample.json");
    List<Book> books = jsonMarshaller.fromJsonArray(booksJson, Book.class);

    @Test
    public void findCheapestBookStream() {
        Optional<BigDecimal> price = books.stream().map(book -> book.price).min(BigDecimal::compareTo);
        assertThat(price, is(Optional.of(BigDecimal.valueOf(5.65))));
    }

    @Test
    public void findAllBooksWrittenByJim() {
        List<String> booksByJim = books.stream()
                .filter(book -> book.authors.contains("Jim Houndface"))
                .map(book -> book.title)
                .collect(Collectors.toList());
        assertThat(booksByJim.size(), is(2));
        assertTrue(booksByJim.contains("Dog Lovers"));
        assertTrue(booksByJim.contains("Cat Lovers"));
    }

    @Test
    public void toDto() {
        assertEquals(3, books.size());
        assertEquals(2, books.get(1).authors.size());
    }
}

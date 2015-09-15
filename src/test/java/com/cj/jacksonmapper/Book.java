package com.cj.jacksonmapper;

import java.math.BigDecimal;
import java.util.List;

public class Book {
    public List<String> authors;
    public BigDecimal price;
    public String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (authors != null ? !authors.equals(book.authors) : book.authors != null) return false;
        if (price != null ? !price.equals(book.price) : book.price != null) return false;
        return !(title != null ? !title.equals(book.title) : book.title != null);

    }

    @Override
    public int hashCode() {
        int result = authors != null ? authors.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "authors=" + authors +
                ", price=" + price +
                ", title='" + title + '\'' +
                '}';
    }
}

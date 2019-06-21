package xyz.itshark.conf.talk.bookstore.micronaut.service;

import xyz.itshark.conf.talk.bookstore.micronaut.pojo.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book addBook(String title);
}

package xyz.itshark.conf.talk.samplemicronaut.service;

import xyz.itshark.conf.talk.samplemicronaut.pojo.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book addBook(String title);
}

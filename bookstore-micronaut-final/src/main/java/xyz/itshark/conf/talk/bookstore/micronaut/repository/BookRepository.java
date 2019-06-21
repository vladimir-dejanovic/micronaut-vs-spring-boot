package xyz.itshark.conf.talk.bookstore.micronaut.repository;

import xyz.itshark.conf.talk.bookstore.micronaut.pojo.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    Book save(String title);
}

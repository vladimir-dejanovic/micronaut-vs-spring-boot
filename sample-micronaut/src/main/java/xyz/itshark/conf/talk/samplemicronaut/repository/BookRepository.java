package xyz.itshark.conf.talk.samplemicronaut.repository;

import xyz.itshark.conf.talk.samplemicronaut.pojo.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    Book save(String title);
}

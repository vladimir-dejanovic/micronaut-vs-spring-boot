package xyz.itshark.conf.talk.bookstore.micronaut.service;

import xyz.itshark.conf.talk.bookstore.micronaut.pojo.Book;
import xyz.itshark.conf.talk.bookstore.micronaut.repository.BookRepository;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(String title) {
        return bookRepository.save(title);
    }
}

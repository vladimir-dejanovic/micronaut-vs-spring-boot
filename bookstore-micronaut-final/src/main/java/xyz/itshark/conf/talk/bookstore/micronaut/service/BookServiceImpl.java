package xyz.itshark.conf.talk.bookstore.micronaut.service;

import xyz.itshark.conf.talk.bookstore.micronaut.repository.BookRepository;
import xyz.itshark.conf.talk.bookstore.micronaut.pojo.Book;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book addBook(String title) {
        return bookRepository.save(title);
    }
}

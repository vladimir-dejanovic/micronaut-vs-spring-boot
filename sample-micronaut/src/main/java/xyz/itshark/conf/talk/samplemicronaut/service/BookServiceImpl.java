package xyz.itshark.conf.talk.samplemicronaut.service;

import xyz.itshark.conf.talk.samplemicronaut.pojo.Book;
import xyz.itshark.conf.talk.samplemicronaut.repository.BookRepository;

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

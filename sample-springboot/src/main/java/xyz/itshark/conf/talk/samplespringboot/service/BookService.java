package xyz.itshark.conf.talk.samplespringboot.service;

import org.springframework.stereotype.Service;
import xyz.itshark.conf.talk.samplespringboot.pojo.Book;
import xyz.itshark.conf.talk.samplespringboot.repository.BookRepository;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BookService {

    @Resource
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }


    public Book addBook(String title) {
        Book book = new Book();
        book.setTitle(title);

        return bookRepository.save(book);
    }
}

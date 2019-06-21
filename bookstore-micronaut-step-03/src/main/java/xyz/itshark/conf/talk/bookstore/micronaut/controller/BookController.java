package xyz.itshark.conf.talk.bookstore.micronaut.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import xyz.itshark.conf.talk.bookstore.micronaut.pojo.Book;
import xyz.itshark.conf.talk.bookstore.micronaut.service.BookService;

import java.util.List;

@Controller
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @Get("/books")
    public List<Book> getAll() {
        return bookService.getAllBooks();
    }

    @Get("/admin/book")
    public Book addBook(@QueryValue("title") String title) {
        return bookService.addBook(title);
    }
}

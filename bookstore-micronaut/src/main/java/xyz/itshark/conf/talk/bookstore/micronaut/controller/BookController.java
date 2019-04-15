package xyz.itshark.conf.talk.bookstore.micronaut.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
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
    @Secured(SecurityRule.IS_ANONYMOUS)
    public List<Book> getAll() {
        return bookService.getAllBooks();
    }

    @Get("/admin/book")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Book addBook(@QueryValue("title") String title) {
        return bookService.addBook(title);
    }
}

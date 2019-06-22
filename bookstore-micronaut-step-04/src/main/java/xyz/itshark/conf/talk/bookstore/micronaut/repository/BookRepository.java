package xyz.itshark.conf.talk.bookstore.micronaut.repository;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.spring.tx.annotation.Transactional;
import xyz.itshark.conf.talk.bookstore.micronaut.pojo.Book;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Singleton
public class BookRepository  {

    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    public BookRepository(@CurrentSession EntityManager entityManager,
                              ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        String sqlString = "Select o from Book as o";
        TypedQuery<Book> query = entityManager.createQuery(sqlString, Book.class);
        return query.getResultList();
    }

    @Transactional
    public Book save(String title) {
        Book book = new Book();
        book.setTitle(title);
        entityManager.persist(book);

        return book;
    }
}

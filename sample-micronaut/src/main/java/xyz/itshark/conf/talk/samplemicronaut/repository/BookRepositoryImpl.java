package xyz.itshark.conf.talk.samplemicronaut.repository;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.spring.tx.annotation.Transactional;
import xyz.itshark.conf.talk.samplemicronaut.pojo.Book;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Singleton
public class BookRepositoryImpl implements  BookRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    public BookRepositoryImpl(@CurrentSession EntityManager entityManager,
                              ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        String sqlString = "Select o from Book as o";
        TypedQuery<Book> query = entityManager.createQuery(sqlString, Book.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Book save(String title) {
        Book book = new Book();
        book.setTitle(title);
        entityManager.persist(book);

        return book;
    }
}

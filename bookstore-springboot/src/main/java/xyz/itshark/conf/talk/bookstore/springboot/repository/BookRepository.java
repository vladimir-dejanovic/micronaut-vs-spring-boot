package xyz.itshark.conf.talk.bookstore.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.itshark.conf.talk.bookstore.springboot.pojo.Book;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
}

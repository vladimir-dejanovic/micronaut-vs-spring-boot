package xyz.itshark.conf.talk.samplespringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.itshark.conf.talk.samplespringboot.pojo.Book;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
}

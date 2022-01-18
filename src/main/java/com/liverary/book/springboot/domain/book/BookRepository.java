package com.liverary.book.springboot.domain.book;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    List <Book> findAll(Sort sort);

    //contains처럼 짜보기
    //like
    @Query(value = "SELECT b FROM book b WHERE b.title like %?1% or b.author like %?1%")
    List <Book> findBySearch( String search);

}
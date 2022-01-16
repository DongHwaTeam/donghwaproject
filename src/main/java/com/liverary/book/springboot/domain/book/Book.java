package com.liverary.book.springboot.domain.book;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Getter
@NoArgsConstructor
@Entity(name = "book")

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookKey;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String author;

    @Column(length = 500, nullable = false)
    private String publisher;

    private String country;

    @Column(length = 500, nullable = false)
    private String bookIntro;

    @Column(length = 500 , nullable = false)
    private String bookCover;

    @Column(nullable = false)
    private String bookContent;

    @Column(nullable = false)
    private int totalPage;

    @Column(nullable = false)
    private Date registeredDate;

    @Column(nullable = false)
    private Date publishedDate;

    @Builder
    public Book(String title, String author, String publisher, String country, String bookIntro, String bookCover, String bookContent, int totalPage, Date registeredDate, Date publishedDate){
        this.title = title ;
        this.author = author;
        this.publisher = publisher;
        this.country = country;
        this.bookIntro  = bookIntro;
        this.bookCover = bookCover;
        this.bookContent = bookContent;
        this.registeredDate = registeredDate;
        this.publishedDate = publishedDate;
        this.totalPage= totalPage;
    }
}
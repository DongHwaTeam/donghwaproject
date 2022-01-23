package com.liverary.book.springboot.web.dto.reading;
import com.liverary.book.springboot.domain.book.Book;
import com.liverary.book.springboot.domain.reading.Reading;
import com.liverary.book.springboot.domain.user.User;
import lombok.Getter;

@Getter
public class ReadingListResponseDto {
    private Long id;
    private Book book;
    private User user;
    private int currentpage;
    private int score;
    private int iswrittenbookreport;
    private String bookreport;

    public ReadingListResponseDto(Reading entity) {
        this.id = entity.getReadingKey();
        this.book = entity.getBook();
        this.user = entity.getUser();
        this.currentpage = entity.getCurrentPage();
        this.score = entity.getScore();
        this.iswrittenbookreport = entity.getIsWrittenBookReport();
        this.bookreport = entity.getBookReport();
    }
}

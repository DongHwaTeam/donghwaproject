package com.liverary.book.springboot.web;

import com.liverary.book.springboot.config.auth.dto.SessionUser;
import com.liverary.book.springboot.domain.book.Book;
import com.liverary.book.springboot.domain.user.User;
import com.liverary.book.springboot.service.BookService;

import com.liverary.book.springboot.service.ReadingService;
import com.liverary.book.springboot.service.UserService;
import com.liverary.book.springboot.service.FileService;
import com.liverary.book.springboot.web.dto.book.BookIntroDto;
import com.liverary.book.springboot.web.dto.book.BookResponseDto;
import com.liverary.book.springboot.web.dto.book.BookUpdateRequestDto;

import com.liverary.book.springboot.web.dto.reading.ReadingListResponseDto;
import com.liverary.book.springboot.web.dto.reading.ReadingResponseDto;
import com.liverary.book.springboot.web.dto.reading.ReadingSaveRequestDto;
import com.liverary.book.springboot.web.dto.reading.ReadingUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Controller
public class IndexController {

    private final BookService bookService;
    private final ReadingService readingService;
    private final HttpSession httpSession;
    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/books/update/{id}")
    public String bookUpdate(@PathVariable Long id, Model model ){
        BookResponseDto bookResponseDto = bookService.findById(id);
        model.addAttribute("book", bookResponseDto);
        return "books-update";
    }
    @GetMapping("/search")
    public String bookSearch(@RequestParam(value="keyword") String keyword, Model model ){
        model.addAttribute("books", bookService.findBySearch(keyword));
        return "search";
    }

    @GetMapping("/")
    public String welcome(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null){
            model.addAttribute("userName", user.getEmail());
        }
        return "welcome";
    }

    @GetMapping("/homepage")
    public String homepage(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null){
            model.addAttribute("userName", user.getEmail());
        }
         model.addAttribute("books",bookService.findAllDesc());

        // readingList??? bookList??? ????????? model??? ??????
        String email = user.getEmail();
        Long userKey = userService.getUserKey(email);
        List<ReadingListResponseDto> readingList = readingService.findAllDesc(userKey,1);
        List<BookResponseDto> bookList = new ArrayList<>();
        for(int i = 0 ; i <(readingList).size(); i++){
            BookResponseDto dto = new BookResponseDto(readingList.get(i).getBook());
            bookList.add(dto);
        }
        model.addAttribute("myBooks",bookList);

        return "home";
    }

    @GetMapping("/book/save")
    public String bookSave(){
        return "post";
    }

    @GetMapping("/book/delete")
    public String bookDelete(){
        return "book-delete";
    }

    @GetMapping("/book/info/{bookKey}")
    public String bookInfo(@PathVariable Long bookKey, Model model){
        BookResponseDto dto = bookService.findById(bookKey);
        model.addAttribute("book",dto);

        return "book-info";
    }

    @GetMapping("/book/content/{bookKey}")
    public String bookContent(@PathVariable Long bookKey, Model model){

        // book-content.mustache??? ?????? ??? ????????? ????????? ????????????
        BookResponseDto bookDto = bookService.findById(bookKey);
        model.addAttribute("book",bookDto);

        // bookKey??? ?????? ?????? reading??? ??????????????? ?????? ??????
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        String email = sessionUser.getEmail();
        Long userKey = userService.getUserKey(email);
        List<ReadingListResponseDto> reading = readingService.findReadingDesc(userKey, bookKey);
        Long readingKey = null;

        if (reading.size()==1){
            // ?????? reading??? ???????????? ??????
            ReadingListResponseDto readingListDto = reading.get(0);
            readingKey = readingListDto.getId();

        }else if(reading.size()>=2){
            // reading??? ???????????? ???????????? ?????? -> ???????????? ??????
            System.out.println("Error : userKey??? bookKey??? ?????? ???????????? reading??? 2??? ???????????????.");

        }else{
            // reading??? ???????????? ?????? ??????(???????????? ???????????? ?????? ?????? ?????????)
            // ?????? bookKey??? userKey??? ?????? reading ?????? ?????? -> startreading
            // Book??? User??? ???????????? ?????? ????????? ????????? ???????????? (V)
            Book currentBook = bookService.findByIdBook(bookKey);
            User currentUser = userService.findByEmailUser(email);

            ReadingSaveRequestDto requestDto = ReadingSaveRequestDto.builder()
                    .book(currentBook)
                    .user(currentUser)
                    .currentPage(1)
                    .score(0)
                    .isWrittenBookReport(0)
                    .bookReport("")
                    .build();
            readingKey = readingService.StartReading(requestDto);
        }

        // ???????????????
        if(readingKey != null){
            ReadingResponseDto readingDto = readingService.findById(readingKey);
            int currentPage = readingDto.getCurrentpage();
            model.addAttribute("reading", readingDto);
            model.addAttribute("readingKey", readingKey);
        }

        return "book-content";
    }

    @GetMapping("/user/info")
    public String myInfo(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null){
            model.addAttribute("userName", user.getEmail());
        }

        // readingList??? bookList??? ????????? model??? ??????
        String email = user.getEmail();
        Long userKey = userService.getUserKey(email);
        List<ReadingListResponseDto> readingList = readingService.findAllDesc(userKey,1);
        List<BookResponseDto> bookList = new ArrayList<>();
        for(int i = 0 ; i <(readingList).size(); i++){
            BookResponseDto dto = new BookResponseDto(readingList.get(i).getBook());
            bookList.add(dto);
        }
        model.addAttribute("reading",bookList);

        return "myinfo";
    }

    @GetMapping("/reading/save_review/{bookKey}")
    public String bookReviewSave(@PathVariable Long bookKey, Model model){
        BookResponseDto dto = bookService.findById(bookKey);

//        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
//        String email = sessionUser.getEmail();
//        User currentUser = userService.findByEmailUser(email);

        model.addAttribute("book",dto);
//
//        // ?????? bookKey??? userKey??? ?????? reading ?????? ?????? -> startreading
//        // Book??? User??? ???????????? ?????? ????????? ????????? ???????????? (V)
        //----

//        ReadingListResponseDto rdto = readingService.findReadingDesc(currentUser.getUserKey(), currentBook.getBookKey()).get(0);
//
//        ReadingUpdateRequestDto requestDto = ReadingUpdateRequestDto.builder()
//                .book(currentBook)
//                .user(currentUser)
//                .currentPage(rdto.getCurrentpage()) // ?????? ????????? ??????
//                .score(rdto.getScore()) //?????? ??????
//                .isWrittenBookReport(1)
//                .bookReport("")
//                .build();
//        readingService.SaveBookReport(rdto.getId(), requestDto);
        return "write-report";
    }

    @GetMapping("/tts")
    public String gettts(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null){
            model.addAttribute("userName", user.getEmail());
        }

        // readingList??? bookList??? ????????? model??? ??????
        String email = user.getEmail();
        Long userKey = userService.getUserKey(email);
        List<ReadingListResponseDto> readingList = readingService.findAllDesc(userKey,1);
        List<BookResponseDto> bookList = new ArrayList<>();
        for(int i = 0 ; i <(readingList).size(); i++){
            BookResponseDto dto = new BookResponseDto(readingList.get(i).getBook());
            bookList.add(dto);
        }
        model.addAttribute("reading",bookList);

        return "tts";
    }



}


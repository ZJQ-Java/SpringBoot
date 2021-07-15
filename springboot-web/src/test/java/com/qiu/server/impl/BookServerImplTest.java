package com.qiu.server.impl;

import com.qiu.entity.mysql.Book;
import com.qiu.server.BookServer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
class BookServerImplTest {

    @Autowired
    BookServer bookServer;

    @Test
    void getAllBooks() {
        System.out.println(bookServer.getAllBooks());
    }

    @Test
    void updateBatch() {
        List<Integer> ids = Arrays.asList(2, 3, 4);
        System.out.println(bookServer.updateBookDetailBatch(ids, 3, "lalala"));
    }

    @Test
    void insert() {
        Book  book = new Book();
        book.setDetail("11cs");
        book.setBookCounts(11);
        book.setBookName("test_1");
        book.setId(11);
        System.out.println(bookServer.addBook(book));
        System.out.println(book);
    }

}
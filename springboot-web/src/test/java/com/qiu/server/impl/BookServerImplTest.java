package com.qiu.server.impl;

import com.google.gson.Gson;
import com.qiu.dao.mysql.mapper.BookMapper;
import com.qiu.entity.mysql.Book;
import com.qiu.entity.mysql.BookExample;
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

    @Autowired
    BookMapper bookMapper;

    @Test
    void getAllBook1s() {
        Object object = bookMapper.selectByExample(new BookExample());
        Gson gson = new Gson();

        System.out.println();
    }

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
    void updateBatch1() {
//        List<Integer> ids = Arrays.asList(2, 3, 4);
        System.out.println(bookServer.updateBookDetail(1, 1, "hha"));
    }

    @Test
    void insert() {
        Book book = new Book();
        book.setDetail("11cs");
        book.setBookCounts(11);
        book.setBookName("test_1");
        book.setId(11);
        System.out.println(bookServer.addBook(book));
        System.out.println(book);
    }

}
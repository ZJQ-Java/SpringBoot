package com.qiu.server.impl;

import com.qiu.server.BookServer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
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
}
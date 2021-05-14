package com.qiu.server.impl;


import com.qiu.dao.mysql.mapper.BookMapper;
import com.qiu.entity.mysql.Book;
import com.qiu.entity.mysql.BookExample;
import com.qiu.server.BookServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BookServerImpl implements BookServer {
    @Autowired
     BookMapper bookMapper;

    @Override
    public List<Book> getAllBooks() {
        BookExample bookExample = new BookExample();
//        bookExample.setColumns("id,book_name");
        return bookMapper.selectByExample(bookExample);
    }
    @Override
    public void testTransaction(Book book) {
    }
}

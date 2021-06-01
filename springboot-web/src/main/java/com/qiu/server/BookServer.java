package com.qiu.server;


import com.qiu.entity.mysql.Book;

import java.util.List;

public interface BookServer {
    public List<Book> getAllBooks();

    void testTransaction(Book book);

    public int updateBookDetailBatch(List<Integer> ids, int bookCount, String detail);
}

package com.qiu.controller;

import com.qiu.entity.mysql.Book;
import com.qiu.server.BookServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController

public class HelloController {
    @Autowired
    BookServer bookServer;
    //    @RequestMapping(value = "/user",method = RequestMethod.GET)
    @GetMapping("/user")
    public String getUser() {
        return "GET-张三";
    }

    //    @RequestMapping(value = "/user",method = RequestMethod.POST)
    @PostMapping("/user")
    public String saveUser() {
        return "POST-张三";
    }


    //    @RequestMapping(value = "/user",method = RequestMethod.PUT)
    @PutMapping("/user")
    public String putUser() {
        return "PUT-张三";
    }

    //    @RequestMapping(value = "/user",method = RequestMethod.DELETE)
    @DeleteMapping("/user")
    public String deleteUser() {
        return "DELETE-张三";
    }

    @GetMapping("/getList")
    public List<Book> getList(){
        return bookServer.getAllBooks();
    }

    public static void main(String[] args) {
        String oaId = "1234";
        StringBuffer sql = new StringBuffer("select group_id,name from tb_vipgroup_info where status =1 ");
        if (oaId != null) {
            sql.append(" and oa_id='").append(oaId).append("'");
        }
        sql.append(" order by ctime");
        System.out.println(sql.toString());
    }

}

package com.qiu.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String thymeleaf(Model model){
        model.addAttribute("name","<h1> hello springboot thymeleaf </h1>");
        model.addAttribute("users", Arrays.asList("zjq","哈哈"));
        return "hello";
    }

}

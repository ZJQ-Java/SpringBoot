package com.qiu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
    @RequestMapping("/")
    public String thymeleaf(Model model){
        model.addAttribute("name","hello springboot thymeleaf");
        return "index";
    }
}

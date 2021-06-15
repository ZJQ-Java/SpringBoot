package com.qiu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@Slf4j
public class FileController {
    @RequestMapping("/upload")
    public String uploadFile(@RequestPart("file") MultipartFile file
            , Model model) throws IOException {
        if (file == null || file.isEmpty()) {
            return "error";
        }
        final String originalFilename = file.getOriginalFilename();
        file.transferTo(new File(System.getProperty("user.dir") + "/springboot-web/tmp/"+originalFilename));
        model.addAttribute("msg", "上传成功");
        return "success";
    }

    @RequestMapping("/uploads")
    public String uploadFiles(@RequestPart("files") MultipartFile[] files
            , Model model) throws IOException {
        if (files == null || files.length == 0) {
            return "error";
        }
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            final String originalFilename = file.getOriginalFilename();
            file.transferTo(new File(System.getProperty("user.dir") + "/springboot-web/tmp/"+originalFilename));
        }

        model.addAttribute("msg", "上传成功");
        return "success";
    }
}

package com.qiu.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ReqController {
    @GetMapping("car/{id}/owner/{username}")
    public Map<String, Object> getPathVariable(@PathVariable("id") Integer id,
                                               @PathVariable("username") String username,
                                               @PathVariable Map<String, String> pathMap,
                                               @RequestHeader("Host") String host,
                                               @RequestHeader Map<String, String> headerMap,
                                               @RequestParam("age") Integer age,
                                               @RequestParam("inters") List<String> inters
    ) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("pathMap", pathMap);

        //-------header--------
//        map.put("header:host", host);
//        map.put("header", headerMap);

        //-----RequestParam-------
        map.put("age", age);
        map.put("inters", inters);

        //----CookieValue--------
        return map;
    }

    @PostMapping("/save")
    public Map<String, Object> postBody(@RequestBody String content) {

        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        return map;
    }

    //1、语法： 请求路径：/cars/sell;low=34;brand=byd,audi,yd
    //2、SpringBoot默认是禁用了矩阵变量的功能
    //      手动开启：原理。对于路径的处理。UrlPathHelper进行解析。
    //              removeSemicolonContent（移除分号内容）支持矩阵变量的
    //3、矩阵变量必须有url路径变量才能被解析
    @GetMapping("/cars/{path}")
    public Map<String, Object> carsSell(@MatrixVariable("low") Integer low,
                                        @MatrixVariable("brand") List<String> brand,
                                        @PathVariable("path") String path) {
        Map<String, Object> map = new HashMap<>();

        map.put("low", low);
        map.put("brand", brand);
        map.put("path", path);
        return map;
    }

    // /boss/1;age=20/2;age=10

    @GetMapping("/boss/{bossId}/{empId}")
    public Map boss(@MatrixVariable(value = "age",pathVar = "bossId") Integer bossAge,
                    @MatrixVariable(value = "age",pathVar = "empId") Integer empAge){
        Map<String,Object> map = new HashMap<>();

        map.put("bossAge",bossAge);
        map.put("empAge",empAge);
        return map;

    }

}
